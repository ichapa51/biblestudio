package org.biblestudio.client;

import java.util.ArrayList;
import java.util.List;

import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;
/**
 * 
 * @author Israel Chapa
 * @since 04/08/2011
 */
public abstract class StatusDispatcher {

	protected List<ActionStatusListener> listeners;
	protected Exception lastError;
	
	public StatusDispatcher() {
		listeners = new ArrayList<ActionStatusListener>(getInitialCapacity());
	}
	
	protected int getInitialCapacity() {
		return 1;
	}
	
	public void addActionStatusListener(ActionStatusListener listener) {
		listeners.add(listener);
	}
	
	public void removeActionStatusListener(ActionStatusListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireActionCompleted(Tag tag, boolean success) {
		if (listeners != null && listeners.size() > 0) {
			CompleteStatusEvent e = new CompleteStatusEvent(this, tag, success);
			for (int i = listeners.size() - 1; i >= 0; i--) {
				listeners.get(i).actionCompleted(e);
			}
		}
	}
	
	protected void fireMessageSent(Tag tag, String message) {
		if (listeners != null && listeners.size() > 0) {
			MessageStatusEvent e = new MessageStatusEvent(this, tag, message);
			for (int i = listeners.size() - 1; i >= 0; i--) {
				listeners.get(i).messageSent(e);
			}
		}
	}
	
	protected void firePercentDone(Tag tag, double percent) {
		if (listeners != null && listeners.size() > 0) {
			PercentStatusEvent e = new PercentStatusEvent(this, tag, percent);
			for (int i = listeners.size() - 1; i >= 0; i--) {
				listeners.get(i).percentDone(e);
			}
		}
	}
	
	protected void fireErrorFound(Tag tag, Exception error) {
		lastError = error;
		if (listeners != null && listeners.size() > 0) {
			ErrorStatusEvent e = new ErrorStatusEvent(this, tag, error);
			for (int i = listeners.size() - 1; i >= 0; i--) {
				listeners.get(i).errorFound(e);
			}
		}
	}
	
	public Exception getLastError() {
		return lastError;
	}
	
	public void clearLastError() {
		lastError = null;
	}
}
