package org.biblestudio.client.db;

import org.biblestudio.client.TagObject;

public class CloseCommand extends DataCommand<TagObject> {

	public CloseCommand(BibleDbEngine engine, TagObject input) {
		super(engine, input);
	}

	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Shuting down " + dbEngine.getName() + " engine...");
			dbEngine.shutdown(getInput());
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}