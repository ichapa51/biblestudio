package org.biblestudio.client.db;

import org.biblestudio.client.SearchQuery;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 20/07/2011
 */
public class SearchCommand extends DataCommand<SearchQuery> {

	public SearchCommand(BibleDbEngine engine, SearchQuery input) {
		super(engine, input);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.Command#execute()
	 */
	@Override
	public void execute() {
		boolean success = false;
		try {
			fireMessageSent("Executing Search Command: " + getInput().getText());
			dbEngine.searchQuery(getInput());
			success = true;
		} catch (Exception e) {
			fireErrorFound(e);
		}
		fireActionCompleted(success);
	}
}