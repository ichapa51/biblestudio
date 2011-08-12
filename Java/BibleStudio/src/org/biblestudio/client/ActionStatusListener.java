package org.biblestudio.client;

import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;

public interface ActionStatusListener {

	void actionCompleted(CompleteStatusEvent event);
	
	void messageSent(MessageStatusEvent event);
	
	void percentDone(PercentStatusEvent event);
	
	void errorFound(ErrorStatusEvent event);
}
