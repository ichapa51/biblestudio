package org.biblestudio.client.event;

import org.biblestudio.client.Tag;

public class ErrorStatusEvent extends ActionStatusEvent {

	private Exception error;
	
	public ErrorStatusEvent(Object source, Tag tag, Exception error) {
		super(source, tag);
		this.error = error;
	}

	public Exception getError() {
		return error;
	}
}
