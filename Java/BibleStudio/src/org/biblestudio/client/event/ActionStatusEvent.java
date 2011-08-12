package org.biblestudio.client.event;

import java.util.Date;

import org.biblestudio.client.Tag;

public class ActionStatusEvent {

	private Object source;
	private Date time;
	private Tag tagObject;
	
	public ActionStatusEvent(Object source, Tag tagObject) {
		this.source = source;
		this.tagObject = tagObject;
		this.time = new Date();
	}

	public Object getSource() {
		return source;
	}

	public Date getTime() {
		return time;
	}

	public Tag getTagObject() {
		return tagObject;
	}
	
	public Object getTag() {
		if (tagObject != null) {
			return tagObject.getTag();
		}
		return null;
	}
}
