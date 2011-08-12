package org.biblestudio.client.db;

import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.client.IdQuery;
import org.biblestudio.client.ImportMode;
import org.biblestudio.client.IndexQuery;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.TagObject;
import org.biblestudio.model.Bible;

public interface BibleDbEngine {

	String getName();
	
	ConnectionRequest getConnectionConfig();
	
	void start(ConnectionRequest config) throws Exception;
	
	boolean isRunning();
	
	void shutdown(TagObject obj) throws Exception;
	
	AddBibleTransaction createAddBibleTransaction(Bible bible, ImportMode mode) throws Exception;

	void idQuery(IdQuery query) throws Exception;
	
	void modelQuery(ModelQuery query) throws Exception;
	
	void searchQuery(SearchQuery query) throws Exception;
	
	void indexQuery(IndexQuery query) throws Exception;
}
