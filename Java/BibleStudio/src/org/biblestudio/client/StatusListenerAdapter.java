package org.biblestudio.client;

import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 28/07/2011
 */
public class StatusListenerAdapter implements ActionStatusListener {

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#actionCompleted(org.ebible.client.event.CompleteStatusEvent)
	 */
	@Override
	public void actionCompleted(CompleteStatusEvent event) {
		
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#messageSent(org.ebible.client.event.MessageStatusEvent)
	 */
	@Override
	public void messageSent(MessageStatusEvent event) {
		
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#percentDone(org.ebible.client.event.PercentStatusEvent)
	 */
	@Override
	public void percentDone(PercentStatusEvent event) {
		
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#errorFound(org.ebible.client.event.ErrorStatusEvent)
	 */
	@Override
	public void errorFound(ErrorStatusEvent event) {
		
	}
}