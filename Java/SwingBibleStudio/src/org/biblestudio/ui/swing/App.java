package org.biblestudio.ui.swing;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import org.biblestudio.client.ActionStatusListener;
import org.biblestudio.client.BibleDataClient;
import org.biblestudio.client.Command;
import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.client.ModelCache;
import org.biblestudio.client.ProxyDataClient;
import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;
import org.biblestudio.model.Bible;
import org.biblestudio.ui.swing.event.AppEvent;
import org.biblestudio.ui.swing.event.AppEventType;
import org.biblestudio.ui.swing.resx.AppResources;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 29/07/2011
 */
public class App implements AppContext, WindowListener, ActionStatusListener {

	protected static App context;
	private List<AppListener> listeners;
	private ModelCache modelCache;
	private AppConfig config;
	private AppResources appResources;
	private ProxyDataClient dataClient;
	private BibleStudio mainFrame;
	private List<Bible> bibles;
	
	protected App() {
		
	}
	
	public static AppContext getContext() {
		return context;
	}
	
	@Override
	public String getAppTitle() {
		return "Bible Studio 0.9 Beta";
	}
	
	@Override
	public List<Bible> getBibles(Boolean isTemplate) {
		if (bibles == null) {
			return null;
		}
		if (isTemplate == null) {
			return bibles;
		}
		List<Bible> list = new ArrayList<Bible>();
		for (Bible bible : bibles) {
			if (isTemplate && bible.getIsTemplate()) {
				list.add(bible);
			} else if (!isTemplate && !bible.getIsTemplate()) {
				list.add(bible);
			}
		}
		return list;
	}
	
	@Override
	public List<Bible> getBibles(int idTemplate) {
		if (bibles == null) {
			return null;
		}
		List<Bible> list = new ArrayList<Bible>();
		for (Bible bible : bibles) {
			if (bible.getIdTemplate() != null && bible.getIdTemplate().intValue() == idTemplate) {
				list.add(bible);
			}
		}
		return list;
	}
	
	@Override
	public Bible getDefaultBible() {
		List<Bible> bibles = App.getContext().getBibles(false);
		if (bibles != null && bibles.size() > 0) {
			return bibles.get(0);
		}
		return null;
	}
	
	@Override
	public BibleStudio getMainFrame() {
		return mainFrame;
	}
	
	@Override
	public AppConfig getConfiguration() {
		return config;
	}
	
	@Override
	public AppResources getResources() {
		return appResources;
	}
	
	@Override
	public BibleDataClient getDataClient() {
		return dataClient;
	}
	
	@Override
	public ModelCache getModelCache() {
		return modelCache;
	}
	
	@Override
	public void addAppListener(AppListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeAppListener(AppListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		
	}
	
	@Override
	public void exit() {
		windowClosing(null);
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		config.save();
		closeDataClient();
		mainFrame.dispose();
	}

	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	@Override
	public void actionCompleted(CompleteStatusEvent e) {
		if (e.isSuccess()) {
			if (e.getTagObject() != null) {
				ConnectionRequest conn = (ConnectionRequest) e.getTagObject();
				if (conn.getResponse() != null) {
					bibles = conn.getResponse().getBibles();
				}
			}
			setStatus("Database succesfully started");
			this.fireAppEvent(AppEventType.DataSourceChanged);
		}
	}

	@Override
	public void messageSent(MessageStatusEvent event) {
		setStatus(event.getMessage());
	}

	@Override
	public void percentDone(PercentStatusEvent event) {
		
	}

	@Override
	public void errorFound(ErrorStatusEvent event) {
		event.getError().printStackTrace();
		mainFrame.showError(event.getError());
	}
	
	@Override
	public void setStatus(String status) {
		mainFrame.setStatus(status);
	}
	
	@Override
	public void showError(Exception error) {
		showError(error.getMessage());
	}
	
	@Override
	public void showError(String message) {
		mainFrame.showError(message);
	}
	
	@Override
	public void restart() {
		closeDataClient();
		modelCache.clear();
		startDataClient();
	}
	
	@Override
	public BibleDesktop getDesktop() {
		return mainFrame.getDesktop();
	}
	
	protected void fireAppEvent(AppEventType type) {
		AppEvent event = new AppEvent(type);
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).applicationChanged(event);
		}
	}
	
	protected void startDataClient() {
		try {
			setStatus("Starting data client...");
			String clientImpl = config.getDataClientImpl();
			BibleDataClient client = (BibleDataClient) Class.forName(clientImpl).newInstance();
			Command<ConnectionRequest> openCmd = null;
			if (dataClient == null) {
				dataClient = new ProxyDataClient(client);
				openCmd = dataClient.createOpenCommand(config.getConnectionConfig());
			} else {
				dataClient.setDataClient(client);
				openCmd = dataClient.createOpenCommand(config.getConnectionConfig());
			}
			openCmd.addActionStatusListener(this);
			openCmd.executeAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void start() {
		listeners = new ArrayList<AppListener>();
		modelCache = new ModelCache();
		config = new AppConfig();
		appResources = new AppResources();
		mainFrame = new BibleStudio();
		mainFrame.addWindowListener(this);
		mainFrame.setVisible(true);
		mainFrame.getDesktop().newWindow();
		startDataClient();
	}
	
	protected void closeDataClient() {
		if (getDataClient() != null) {
			Command<?> cmd = dataClient.createCloseCommand(null);
			cmd.execute();
		}
	}
	
	private static void createAndShowGUI(String[] args) {
		context = new App();
		context.start();
	}
	
	public static void main(final String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(args);
            }
        });
    }
}