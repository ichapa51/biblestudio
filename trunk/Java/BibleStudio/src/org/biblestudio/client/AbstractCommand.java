package org.biblestudio.client;
/**
 * 
 * @author	Israel Chapa
 */
public abstract class AbstractCommand<E extends Tag> extends StatusDispatcher implements Command<E> {

	private static long idCount = 0;
	private int priority = Thread.MIN_PRIORITY;
	private long id = 0;
	protected E input;
	
	public AbstractCommand() {
		idCount++;
		id = idCount;
	}
	
	public AbstractCommand(E input) {
		idCount++;
		id = idCount;
		this.input = input;
	}
	
	@Override
	public Command<E> setActionStatus(ActionStatusListener listener) {
		if (listener == null) {
			while (listeners.size() > 0) {
				listeners.remove(0);
			}
			return null;
		} else {
			super.addActionStatusListener(listener);
			return this;
		}
	}
	
	@Override
	public void setAsyncPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public int getAsyncPriority() {
		return priority;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public E getInput() {
		return input;
	}

	@Override
	public void setInput(E input) {
		this.input = input;
	}
	
	protected void fireActionCompleted(boolean success) {
		super.fireActionCompleted(getInput(), success);
	}
	
	protected void fireMessageSent(String message) {
		super.fireMessageSent(getInput(), message);
	}
	
	protected void firePercentDone(double percent) {
		super.firePercentDone(getInput(), percent);
	}
	
	protected void fireErrorFound(Exception error) {
		super.fireErrorFound(getInput(), error);
	}
	
	@Override
	public void executeAsync() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				execute();
			}
		});
		thread.setPriority(getAsyncPriority());
		thread.start();
	}
}