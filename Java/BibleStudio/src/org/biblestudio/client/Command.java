package org.biblestudio.client;

public interface Command<E extends Tag> {

	long getId();
	
	void addActionStatusListener(ActionStatusListener listener);
	
	void removeActionStatusListener(ActionStatusListener listener);
	
	E getInput();
	
	void setInput(E input);
	
	void execute();
	
	void executeAsync();
	
	public void setAsyncPriority(int priority);

	public int getAsyncPriority();
}
