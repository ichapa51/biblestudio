package org.biblestudio.client;

import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;

public class DefaultStatusListener implements ActionStatusListener {

	@Override
	public void actionCompleted(CompleteStatusEvent e) {
		String status = e.isSuccess() ? "OK" : "FAILED";
		System.out.println("Action completed: " + status);
	}

	@Override
	public void messageSent(MessageStatusEvent e) {
		System.out.println(e.getMessage());
	}

	@Override
	public void percentDone(PercentStatusEvent e) {
		if (e.getPercent() != null) {
			System.out.println(e.getPercent().intValue() + "%");
		}
	}

	@Override
	public void errorFound(ErrorStatusEvent e) {
		e.getError().printStackTrace();
	}
}