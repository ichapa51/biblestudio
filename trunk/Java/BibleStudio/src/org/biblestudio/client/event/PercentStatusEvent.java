package org.biblestudio.client.event;

import org.biblestudio.client.Tag;

public class PercentStatusEvent extends ActionStatusEvent {

	private Double percent;
	
	public PercentStatusEvent(Object source, Tag tag, Double percent) {
		super(source, tag);
		this.percent = percent;
	}

	public Double getPercent() {
		return percent;
	}
}
