package org.biblestudio.client.db;

import org.biblestudio.client.IdQuery;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class IdQueryCommand extends DataCommand<IdQuery> {

	public IdQueryCommand(BibleDbEngine engine, IdQuery input) {
		super(engine, input);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#execute()
	 */
	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Executing IdQuery Command: " + getInput());
			dbEngine.idQuery(getInput());
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}