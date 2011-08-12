package org.biblestudio.client;

public interface BibleDataClient {
	
	Command<ConnectionRequest> createOpenCommand(ConnectionRequest connConfig);
	
	Command<BibleSource> createImportCommand(BibleSource source);
	
	Command<IdQuery> createGetEntityCommand(IdQuery query);
	
	Command<UpdateRequest> createSetEntityCommand(UpdateRequest query);
	
	Command<IdQuery> createRemoveEntityCommand(IdQuery query);
	
	Command<ModelQuery> createModelQueryCommand(ModelQuery query);
	
	Command<SearchQuery> createSearchQueryCommand(SearchQuery query);
	
	Command<IndexQuery> createIndexQueryCommand(IndexQuery query);
	
	Command<FunctionQuery> createFunctionQueryCommand(FunctionQuery query);
	
	Command<TagObject> createCloseCommand(TagObject tag);
}
