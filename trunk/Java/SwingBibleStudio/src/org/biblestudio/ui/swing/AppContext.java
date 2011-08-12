package org.biblestudio.ui.swing;

import java.util.List;

import org.biblestudio.client.BibleDataClient;
import org.biblestudio.client.ModelCache;
import org.biblestudio.model.Bible;
import org.biblestudio.ui.swing.resx.AppResources;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 29/07/2011
 */
public interface AppContext {
	
	String getAppTitle();
	
	void exit();
	
	void restart();
	
	void addAppListener(AppListener listener);
	
	void removeAppListener(AppListener listener);
	
	List<Bible> getBibles(Boolean isTemplate);
	
	List<Bible> getBibles(int idTemplate);
	
	Bible getDefaultBible();
	
	BibleStudio getMainFrame();
	
	BibleDesktop getDesktop();
	
	AppResources getResources();
	
	AppConfig getConfiguration();
	
	BibleDataClient getDataClient();
	
	ModelCache getModelCache();
	
	void setStatus(String status);
	
	void showError(String message);
	
	void showError(Exception error);
}
