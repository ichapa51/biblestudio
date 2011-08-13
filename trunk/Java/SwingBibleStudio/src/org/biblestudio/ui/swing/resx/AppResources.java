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
	
	public String getLanguageEnglish() {
		return getResources().getString("Language.English");
	}
	
	public String getLanguageSpanish() {
		return getResources().getString("Language.Spanish");
	}
	
	public String getMenuFile() {
		return getResources().getString("Menu.File");
	}
	
	public String getMenuFileImport() {
		return getResources().getString("Menu.File.Import");
	}
	
	public String getMenuFileExit() {
		return getResources().getString("Menu.File.Exit");
	}
	
	public String getMenuBible() {
		return getResources().getString("Menu.Bible");
	}
	
	public String getMenuTools() {
		return getResources().getString("Menu.Tools");
	}
	
	public String getMenuToolsOptions() {
		return getResources().getString("Menu.Tools.Options");
	}
	
	public String getMenuWindow() {
		return getResources().getString("Menu.Window");
	}
	
	public String getMenuWindowNew() {
		return getResources().getString("Menu.Window.New");
	}
	
	public String getMenuHelp() {
		return getResources().getString("Menu.Help");
	}
	
	public String getMenuHelpAbout() {
		return getResources().getString("Menu.Help.About");
	}
	
	public String getDialogImport() {
		return getResources().getString("Dialog.Import");
	}
	
	public String getDialogOptions() {
		return getResources().getString("Dialog.Options");
	}
	
	public String getLabelContents() {
		return getResources().getString("Label.Contents");
	}
	
	public String getLabelImport() {
		return getResources().getString("Label.Import");
	}
	
	public String getLabelNext() {
		return getResources().getString("Label.Next");
	}
	
	public String getLabelPrevious() {
		return getResources().getString("Label.Previous");
	}
	
	public String getLabelSearch() {
		return getResources().getString("Label.Search");
	}
	
	public String getLabelSelectFile() {
		return getResources().getString("Label.SelectFile");
	}
	
	public String getLabelUILanguage() {
		return getResources().getString("Label.UILanguage");
	}
	
	public String getLabelLookAndFeel() {
		return getResources().getString("Label.LookAndFeel");
	}
	
	public String getButtonAccept() {
		return getResources().getString("Button.Accept");
	}
	
	public String getButtonCancel() {
		return getResources().getString("Button.Cancel");
	}
}
