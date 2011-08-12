package org.biblestudio.client.db;

import org.biblestudio.client.BibleDataClient;
import org.biblestudio.client.BibleSource;
import org.biblestudio.client.Command;
import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.client.FunctionQuery;
import org.biblestudio.client.IdQuery;
import org.biblestudio.client.IndexQuery;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.TagObject;
import org.biblestudio.client.UpdateRequest;

public abstract class AbstractDataClient implements BibleDataClient {

	private BibleDbEngine dbEngine;
	
	protected AbstractDataClient() {
		
	}
	
	public abstract BibleDbEngine createDbEngine();
	
	protected BibleDbEngine getDbEngine() {
		if (dbEngine == null) {
			dbEngine = createDbEngine();
		}
		return dbEngine;
	}

	@Override
	public Command<ConnectionRequest> createOpenCommand(ConnectionRequest connConfig) {
		return new OpenCommand(getDbEngine(), connConfig);
	}

	@Override
	public Command<BibleSource> createImportCommand(BibleSource input) {
		return new ImportCommand(getDbEngine(), input);
	}

	@Override
	public Command<IdQuery> createGetEntityCommand(IdQuery query) {
		return new IdQueryCommand(getDbEngine(), query);
	}

	@Override
	public Command<UpdateRequest> createSetEntityCommand(UpdateRequest query) {
		throw new UnsupportedOperationException(); //TODO
	}

	@Override
	public Command<IdQuery> createRemoveEntityCommand(IdQuery query) {
		throw new UnsupportedOperationException();  //TODO
	}

	@Override
	public Command<ModelQuery> createModelQueryCommand(ModelQuery query) {
		return new ModelQueryCommand(getDbEngine(), query);
	}

	@Override
	public Command<SearchQuery> createSearchQueryCommand(SearchQuery query) {
		return new SearchCommand(getDbEngine(), query);
	}
	
	@Override
	public Command<IndexQuery> createIndexQueryCommand(IndexQuery query) {
		return new IndexQueryCommand(getDbEngine(), query);
	}

	@Override
	public Command<TagObject> createCloseCommand(TagObject tag) {
		return new CloseCommand(getDbEngine(), tag);
	}
	
	@Override
	public Command<FunctionQuery> createFunctionQueryCommand(FunctionQuery query) {
		throw new UnsupportedOperationException();  //TODO
	}
}
