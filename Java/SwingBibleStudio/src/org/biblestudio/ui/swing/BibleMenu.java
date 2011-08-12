package org.biblestudio.ui.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
		JMenu menu = new JMenu(resx.getMenuFileName());
		//menu.setMnemonic(KeyEvent.VK_F);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		//menuItem.setActionCommand("new");
		this.add(menu);
		JMenuItem menuItem = new JMenuItem(resx.getMenuFileImportName());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportDialog.showDialog();
			}
		});
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem(resx.getMenuFileExitName());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				App.getContext().exit();
			}
		});
        menu.add(menuItem);
        // Bible Menu
        bibleMenu = new JMenu(resx.getMenuBibleName());
        this.add(bibleMenu);
        bibleMenu.setEnabled(false);
        // Window Menu
        menu = new JMenu(resx.getMenuWindowName());
        this.add(menu);
        menuItem = new JMenuItem(resx.getMenuWindowNewName());
		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				App.getContext().getMainFrame().getDesktop().newWindow();
			}
		});
        menu.add(menuItem);
        //Help Menu
        menu = new JMenu(resx.getMenuHelpName());
        this.add(menu);
        menuItem = new JMenuItem(resx.getMenuHelpAboutName());
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
				App.getContext().getResources().getMenuHelpAboutName(),
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
