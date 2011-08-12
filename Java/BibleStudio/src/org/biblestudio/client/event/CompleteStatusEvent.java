package org.biblestudio.client.event;

import org.biblestudio.client.Tag;

public class CompleteStatusEvent extends ActionStatusEvent {

	private boolean success;
	
	public CompleteStatusEvent(Object source, Tag tag, boolean success) {
		super(source, tag);
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}
}
