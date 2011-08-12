package org.biblestudio.ui.swing.resx;

import java.util.ResourceBundle;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 29/07/2011
 */
public class AppResources {

	protected static final String RESOURCES = "org.biblestudio.ui.swing.resx.Resources";
	
	public AppResources() {
		
	}
	
	protected ResourceBundle getResources() {
		return ResourceBundle.getBundle(RESOURCES);
	}
	
	public String getMenuFileName() {
		return getResources().getString("Menu.File");
	}
	
	public String getMenuFileImportName() {
		return getResources().getString("Menu.File.Import");
	}
	
	public String getMenuFileExitName() {
		return getResources().getString("Menu.File.Exit");
	}
	
	public String getMenuBibleName() {
		return getResources().getString("Menu.Bible");
	}
	
	public String getMenuWindowName() {
		return getResources().getString("Menu.Window");
	}
	
	public String getMenuWindowNewName() {
		return getResources().getString("Menu.Window.New");
	}
	
	public String getMenuHelpName() {
		return getResources().getString("Menu.Help");
	}
	
	public String getMenuHelpAboutName() {
		return getResources().getString("Menu.Help.About");
	}
}
