package org.biblestudio.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.biblestudio.model.Bible;
import org.biblestudio.ui.swing.event.AppEvent;
import org.biblestudio.ui.swing.resx.AppResources;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BibleMenu extends JMenuBar implements AppListener {
	
	private JMenu bibleMenu;
	
	public BibleMenu() {
		createMenu();
		App.getContext().addAppListener(this);
	}
	
	protected void createMenu() {
		AppResources resx = App.getContext().getResources();
		// File Menu
		JMenu menu = new JMenu(resx.getMenuFile());
		//menu.setMnemonic(KeyEvent.VK_F);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		//menuItem.setActionCommand("new");
		this.add(menu);
		JMenuItem menuItem = new JMenuItem(resx.getMenuFileImport());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportDialog.showDialog();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem(resx.getMenuFileExit());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				App.getContext().exit();
			}
		});
        menu.add(menuItem);
        // Bible Menu
        bibleMenu = new JMenu(resx.getMenuBible());
        this.add(bibleMenu);
        bibleMenu.setEnabled(false);
        // Tools Menu
        menu = new JMenu(resx.getMenuTools());
        this.add(menu);
        menuItem = new JMenuItem(resx.getMenuToolsOptions());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionsDialog.showDialog();
			}
		});
		menu.add(menuItem);
        // Window Menu
        menu = new JMenu(resx.getMenuWindow());
        this.add(menu);
        if (new JFrame().isAlwaysOnTopSupported()) {
        	menuItem = new JCheckBoxMenuItem(resx.getMenuWindowAlwaysOnTop());
     		menuItem.addActionListener(new ActionListener() {
     			@Override
     			public void actionPerformed(ActionEvent e) {
     				boolean aot = App.getContext().getMainFrame().isAlwaysOnTop();
     				App.getContext().getMainFrame().setAlwaysOnTop(!aot);
     			}
     		});
     		menu.add(menuItem);
        }
        menuItem = new JMenuItem(resx.getMenuWindowNew());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				App.getContext().getMainFrame().getDesktop().newWindow();
			}
		});
        menu.add(menuItem);
        //Help Menu
        menu = new JMenu(resx.getMenuHelp());
        this.add(menu);
        menuItem = new JMenuItem(resx.getMenuHelpAbout());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showAbout();
			}
		});
        menu.add(menuItem);
	}
	
	protected void showAbout() {
		JOptionPane.showMessageDialog(App.getContext().getMainFrame(),
				App.getContext().getAppTitle() + " 2011",
				App.getContext().getResources().getMenuHelpAbout(),
				JOptionPane.INFORMATION_MESSAGE);
	}
	
	protected void refreshBibleMenu() {
		bibleMenu.removeAll();
		List<Bible> templates = App.getContext().getBibles(true);
		boolean hasItems = false;
		if (templates != null) {
			for (Bible template : templates) {
				JMenu templateMenu = new JMenu(template.getTitle());
				hasItems = false;
				for (Bible bible : App.getContext().getBibles(template.getId())) {
					JMenuItem menuItem = new JMenuItem(bible.getName());
					final Bible b = bible;
					menuItem.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							App.getContext().getMainFrame().getDesktop().newWindow(b);
						}
					});
					templateMenu.add(menuItem);
					hasItems = true;
				}
				if (hasItems) {
					bibleMenu.add(templateMenu);
				}
			}
		}
		bibleMenu.setEnabled(bibleMenu.getMenuComponentCount() > 0);
	}

	@Override
	public void applicationChanged(AppEvent event) {
		refreshBibleMenu();
	}
}
