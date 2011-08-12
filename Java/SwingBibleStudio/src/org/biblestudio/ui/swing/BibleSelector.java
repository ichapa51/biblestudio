package org.biblestudio.ui.swing;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.biblestudio.model.Bible;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 12/08/2011
 */
@SuppressWarnings("serial")
public class BibleSelector extends JPanel {

	private JComboBox templateBox;
	private JComboBox bibleBox;
	
	public BibleSelector() {
		templateBox = new JComboBox();
		bibleBox = new JComboBox();
	}
	
	public Bible getSelectedBible() {
		return null;
	}
}
