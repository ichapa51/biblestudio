package org.biblestudio.ui.swing;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

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
}
