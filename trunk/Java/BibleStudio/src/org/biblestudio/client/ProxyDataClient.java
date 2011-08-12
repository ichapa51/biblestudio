package org.biblestudio.client;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 05/08/2011
 */
public class ProxyDataClient implements BibleDataClient {

	private BibleDataClient client;
	
	public ProxyDataClient(BibleDataClient client) {
		this.client = client;
	}
	
	public BibleDataClient getDataClient() {
		return client;
	}
	
	public void setDataClient(BibleDataClient client) {
		this.client = client;
	}
	
	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createOpenCommand(org.ebible.client.ConnectionConfig)
	 */
	@Override
	public Command<ConnectionRequest> createOpenCommand(
			ConnectionRequest connConfig) {
		return new ProxyCommand<ConnectionRequest>(ProxyCommand.CommandType.OPEN,
				this, client.createOpenCommand(connConfig));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createImportCommand(org.ebible.client.BibleSource)
	 */
	@Override
	public Command<BibleSource> createImportCommand(BibleSource source) {
		return new ProxyCommand<BibleSource>(ProxyCommand.CommandType.IMPORT,
				this, client.createImportCommand(source));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createGetEntityCommand(org.ebible.client.IdQuery)
	 */
	@Override
	public Command<IdQuery> createGetEntityCommand(IdQuery query) {
		return new ProxyCommand<IdQuery>(ProxyCommand.CommandType.GET,
				this, client.createGetEntityCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createSetEntityCommand(org.ebible.client.UpdateRequest)
	 */
	@Override
	public Command<UpdateRequest> createSetEntityCommand(UpdateRequest query) {
		return new ProxyCommand<UpdateRequest>(ProxyCommand.CommandType.SET,
				this, client.createSetEntityCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createRemoveEntityCommand(org.ebible.client.IdQuery)
	 */
	@Override
	public Command<IdQuery> createRemoveEntityCommand(IdQuery query) {
		return new ProxyCommand<IdQuery>(ProxyCommand.CommandType.REMOVE,
				this, client.createRemoveEntityCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createModelQueryCommand(org.ebible.client.ModelQuery)
	 */
	@Override
	public Command<ModelQuery> createModelQueryCommand(ModelQuery query) {
		return new ProxyCommand<ModelQuery>(ProxyCommand.CommandType.MODEL,
				this, client.createModelQueryCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createSearchQueryCommand(org.ebible.client.SearchQuery)
	 */
	@Override
	public Command<SearchQuery> createSearchQueryCommand(SearchQuery query) {
		return new ProxyCommand<SearchQuery>(ProxyCommand.CommandType.SEARCH,
				this, client.createSearchQueryCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createFunctionQueryCommand(org.ebible.client.FunctionQuery)
	 */
	@Override
	public Command<FunctionQuery> createFunctionQueryCommand(FunctionQuery query) {
		return new ProxyCommand<FunctionQuery>(ProxyCommand.CommandType.FUNCTION,
				this, client.createFunctionQueryCommand(query));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createCloseCommand(org.ebible.client.TagObject)
	 */
	@Override
	public Command<TagObject> createCloseCommand(TagObject tag) {
		return new ProxyCommand<TagObject>(ProxyCommand.CommandType.CLOSE,
				this, client.createCloseCommand(tag));
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleDataClient#createSearchQueryCommand(org.ebible.client.IndexQuery)
	 */
	@Override
	public Command<IndexQuery> createIndexQueryCommand(IndexQuery query) {
		return new ProxyCommand<IndexQuery>(ProxyCommand.CommandType.INDEX,
				this, client.createIndexQueryCommand(query));
	}

}
