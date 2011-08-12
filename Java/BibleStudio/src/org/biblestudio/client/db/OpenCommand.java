package org.biblestudio.client.db;

import org.biblestudio.client.ConnectionRequest;

public class OpenCommand extends DataCommand<ConnectionRequest> {

	public OpenCommand(BibleDbEngine engine, ConnectionRequest input) {
		super(engine, input);
	}

	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Starting database engine...");
			dbEngine.start(getInput());
			if (getInput() == null) {
				this.setInput(dbEngine.getConnectionConfig());
			}
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}