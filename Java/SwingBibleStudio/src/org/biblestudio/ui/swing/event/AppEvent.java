package org.biblestudio.ui.swing.event;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 05/08/2011
 */
public class AppEvent {

	private AppEventType eventType;
	
	public AppEvent(AppEventType type) {
		this.eventType = type;
	}

	public AppEventType getEventType() {
		return eventType;
	}
}