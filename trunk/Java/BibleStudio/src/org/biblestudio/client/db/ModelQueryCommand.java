package org.biblestudio.client.db;

import org.biblestudio.client.ModelQuery;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class ModelQueryCommand extends DataCommand<ModelQuery> {

	public ModelQueryCommand(BibleDbEngine engine, ModelQuery input) {
		super(engine, input);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#execute()
	 */
	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Executing ModelQuery Command: " + getInput());
			dbEngine.modelQuery(getInput());
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}