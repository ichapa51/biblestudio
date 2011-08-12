package org.biblestudio.ui.swing;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.biblestudio.model.Bible;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BibleSplitPane extends JPanel implements PropertyChangeListener {

	private String title;
	private BiblePanel firstPanel;
	private BiblePanel secondPanel;
	
	public BibleSplitPane() {
		init(null);
	}
	
	public BibleSplitPane(Bible bible) {
		init(bible);
	}
	
	protected void init(Bible bible) {
		setLayout(new BorderLayout());
		this.title = "Bible";
		firstPanel = new BiblePanel(bible);
		firstPanel.addPropertyChangeListener("Bible", this);
		this.add(firstPanel, BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		String oldTitle = this.getTitle();
		String title = "Bible";
		if (firstPanel.getBible() != null) {
			title = firstPanel.getBible().getName();
		}
		setTitle(title);
		this.firePropertyChange("Title", oldTitle, title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void dispose() {
		firstPanel.dispose();
	}
}
