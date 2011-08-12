package org.biblestudio.client.event;

import org.biblestudio.client.Tag;

public class MessageStatusEvent extends ActionStatusEvent {

	private String message;
	
	public MessageStatusEvent(Object source, Tag tag, String message) {
		super(source, tag);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}