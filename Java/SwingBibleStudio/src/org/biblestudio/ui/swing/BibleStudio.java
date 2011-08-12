package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.biblestudio.ui.swing.util.BetterJFrame;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BibleStudio extends BetterJFrame {

	private BibleMenu menu;
	private BibleDesktop desktop;
	private JLabel statusLabel;
	
	public BibleStudio() {
		super(App.getContext().getAppTitle());
		statusLabel = new JLabel();
		menu = new BibleMenu();
		desktop = new BibleDesktop();
		this.setContentPane(createContentPane());
		this.setJMenuBar(menu);
	}
	
	public BibleDesktop getDesktop() {
		return desktop;
	}
	
	protected Container createContentPane() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(desktop, BorderLayout.CENTER);
		panel.add(statusLabel, BorderLayout.SOUTH);
		return panel;
	}
	
	public void showError(Exception e) {
		showError(e.getMessage());
	}
	
	public void showError(String error) {
		JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void setStatus(String status) {
		statusLabel.setText(status);
	}

    public static void main(String[] args) {
       App.main(args);
    }
}
