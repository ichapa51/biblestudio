package org.biblestudio.client.db;

import org.biblestudio.client.IndexQuery;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 10/08/2011
 */
public class IndexQueryCommand extends DataCommand<IndexQuery> {

	public IndexQueryCommand(BibleDbEngine engine, IndexQuery input) {
		super(engine, input);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#execute()
	 */
	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Executing IndexQuery Command: " + getInput());
			dbEngine.indexQuery(getInput());
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}