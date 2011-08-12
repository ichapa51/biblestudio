package org.biblestudio.ui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JInternalFrame;

import org.biblestudio.model.Bible;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
@SuppressWarnings("serial")
public class BibleFrame extends JInternalFrame implements PropertyChangeListener {

	private BibleSplitPane splitPane;
	
	public BibleFrame() {
		super("Bible", true, true, true, true);
		init(null);
	}
	
	public BibleFrame(Bible bible) {
		super("Bible", true, true, true, true);
		init(bible);
	}
	
	protected void init(Bible bible) {
		if (bible != null) {
			this.setTitle(bible.getName());
		}
		this.splitPane = new BibleSplitPane(bible);
		this.splitPane.addPropertyChangeListener("Title", this);
		this.setContentPane(splitPane);
		this.setSize(700, 500);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.setTitle(evt.getNewValue().toString());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
	}
}
