package org.biblestudio.client;

import org.biblestudio.client.event.CompleteStatusEvent;
import org.biblestudio.client.event.ErrorStatusEvent;
import org.biblestudio.client.event.MessageStatusEvent;
import org.biblestudio.client.event.PercentStatusEvent;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 05/08/2011
 */
public class ProxyCommand<E extends Tag> extends StatusDispatcher implements Command<E>, ActionStatusListener {

	public static enum CommandType {
		IMPORT,
		OPEN,
		CLOSE,
		GET,
		SET,
		MODEL,
		SEARCH,
		INDEX,
		FUNCTION,
		REMOVE
	}
	private CommandType type;
	private BibleDataClient dataClient;
	private ProxyDataClient proxyClient;
	private Command<E> cmd;
	
	public ProxyCommand(CommandType type, ProxyDataClient client, Command<E> cmd) {
		this.type = type;
		this.proxyClient = client;
		this.dataClient = proxyClient.getDataClient();
		this.cmd = cmd;
	}
	
	public Command<E> getCommand() {
		return cmd;
	}
	
	public void setCommand(Command<E> cmd) {
		this.cmd = cmd;
		for (ActionStatusListener l : listeners) {
			cmd.addActionStatusListener(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ebible.client.Command#getId()
	 */
	@Override
	public long getId() {
		return cmd.getId();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#addActionStatusListener(org.ebible.client.ActionStatusListener)
	 */
	@Override
	public void addActionStatusListener(ActionStatusListener listener) {
		super.addActionStatusListener(listener);
		cmd.addActionStatusListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#removeActionStatusListener(org.ebible.client.ActionStatusListener)
	 */
	@Override
	public void removeActionStatusListener(ActionStatusListener listener) {
		super.removeActionStatusListener(listener);
		cmd.removeActionStatusListener(listener);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#getInput()
	 */
	@Override
	public E getInput() {
		return cmd.getInput();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#setInput(org.ebible.client.Tag)
	 */
	@Override
	public void setInput(E input) {
		cmd.setInput(input);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#execute()
	 */
	@Override
	public void execute() {
		if (dataClient != proxyClient.getDataClient()) {
			dataClient = proxyClient.getDataClient();
			setCommand(this.renew(dataClient));
		}
		cmd.execute();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#actionCompleted(org.ebible.client.event.CompleteStatusEvent)
	 */
	@Override
	public void actionCompleted(CompleteStatusEvent event) {
		super.fireActionCompleted(getInput(), event.isSuccess());
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#messageSent(org.ebible.client.event.MessageStatusEvent)
	 */
	@Override
	public void messageSent(MessageStatusEvent event) {
		super.fireMessageSent(getInput(), event.getMessage());
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#percentDone(org.ebible.client.event.PercentStatusEvent)
	 */
	@Override
	public void percentDone(PercentStatusEvent event) {
		super.firePercentDone(getInput(), event.getPercent());
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.ActionStatusListener#errorFound(org.ebible.client.event.ErrorStatusEvent)
	 */
	@Override
	public void errorFound(ErrorStatusEvent event) {
		super.fireErrorFound(getInput(), event.getError());
	}
	
	@SuppressWarnings("unchecked")
	protected Command<E> renew(BibleDataClient client) {
		switch (type) {
			case IMPORT:
				return (Command<E>)client.createImportCommand((BibleSource)cmd.getInput());
			case OPEN:
				return (Command<E>)client.createOpenCommand((ConnectionRequest)cmd.getInput());
			case CLOSE:
				return (Command<E>)client.createCloseCommand((TagObject)cmd.getInput());
			case GET:
				return (Command<E>)client.createGetEntityCommand((IdQuery)cmd.getInput());
			case SET:
				return (Command<E>)client.createSetEntityCommand((UpdateRequest)cmd.getInput());
			case MODEL:
				return (Command<E>)client.createModelQueryCommand((ModelQuery)cmd.getInput());
			case SEARCH:
				return (Command<E>)client.createSearchQueryCommand((SearchQuery)cmd.getInput());
			case INDEX:
				return (Command<E>)client.createIndexQueryCommand((IndexQuery)cmd.getInput());
			case FUNCTION:
				return (Command<E>)client.createFunctionQueryCommand((FunctionQuery)cmd.getInput());
			case REMOVE:
				return (Command<E>)client.createRemoveEntityCommand((IdQuery)cmd.getInput());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#executeAsync()
	 */
	@Override
	public void executeAsync() {
		cmd.executeAsync();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#setAsyncPriority(int)
	 */
	@Override
	public void setAsyncPriority(int priority) {
		cmd.setAsyncPriority(priority);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#getAsyncPriority()
	 */
	@Override
	public int getAsyncPriority() {
		return cmd.getAsyncPriority();
	}
}
