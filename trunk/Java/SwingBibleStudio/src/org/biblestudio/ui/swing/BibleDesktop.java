package org.biblestudio.ui.swing;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.biblestudio.model.Bible;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BibleDesktop extends JDesktopPane {

	public BibleDesktop() {
		//setBackground(ColorUtils.TRANSPARENT);
	}
	
	public void newWindow() {
		newWindow(App.getContext().getDefaultBible());
	}
	
	public void newWindow(Bible bible) {
		BibleFrame frame = new BibleFrame(bible);
		frame.setVisible(true);
		add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {}
	}
	
	public void closeAll() {
		JInternalFrame[] frames = this.getAllFrames();
		if (frames != null) {
			for (int i = 0; i < frames.length; i++) {
				frames[i].dispose();
			}
		}
	}
	
	public void popupSelectedFrame() {
		JInternalFrame iFrame = this.getSelectedFrame();
		if (iFrame != null) {
			String title = iFrame.getTitle();
			Dimension size = iFrame.getSize();
			Point location = iFrame.getLocationOnScreen();
			Container c = iFrame.getContentPane();
			iFrame.setContentPane(new JPanel());
			iFrame.dispose();
			if (App.getContext().getConfiguration().isCrossPlatformLookAndFeel()) {
				JFrame.setDefaultLookAndFeelDecorated(true);
			}
			JFrame popFrame = new JFrame();
			popFrame.setAlwaysOnTop(App.getContext().getMainFrame().isAlwaysOnTop());
			popFrame.setTitle(title);
			popFrame.setContentPane(c);
			popFrame.setSize(size);
			popFrame.setLocation(location);
			popFrame.setVisible(true);
			if (App.getContext().getConfiguration().isCrossPlatformLookAndFeel()) {
				JFrame.setDefaultLookAndFeelDecorated(false);
			}
		}
	}
}
