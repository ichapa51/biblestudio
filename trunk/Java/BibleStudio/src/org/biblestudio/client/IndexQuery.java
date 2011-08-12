package org.biblestudio.client;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 10/08/2011
 */
public class IndexQuery extends TagObject {

	private String bibleKey;
	private IndexType indexType;
	private int idParagraph;
	private IndexResult result;
	
	public IndexQuery() {
		
	}
	
	public IndexQuery(Object tag) {
		super(tag);
	}
	
	public IndexQuery(Object tag, String bibleKey, IndexType iType, int idParagraph) {
		super(tag);
		this.bibleKey = bibleKey;
		this.indexType = iType;
		this.idParagraph = idParagraph;
	}

	public void setBibleKey(String bibleKey) {
		this.bibleKey = bibleKey;
	}

	public String getBibleKey() {
		return bibleKey;
	}

	public void setIndexType(IndexType indexType) {
		this.indexType = indexType;
	}

	public IndexType getIndexType() {
		return indexType;
	}

	public void setIdParagraph(int idParagraph) {
		this.idParagraph = idParagraph;
	}

	public int getIdParagraph() {
		return idParagraph;
	}

	public void setResult(IndexResult result) {
		this.result = result;
	}

	public IndexResult getResult() {
		return result;
	}
	
	public boolean hasPrevious() {
		return getResult() != null && getResult().hasPrevious();
	}
	
	public IndexQuery getPreviousQuery(Object tag) {
		if (hasPrevious()) {
			IndexQuery query = (IndexQuery) clone();
			query.setTag(tag);
			query.setIdParagraph(getResult().getPreviousId());
			query.setResult(null);
			return query;
		}
		return null;
	}
	
	public boolean hasNext() {
		return getResult() != null && getResult().hasNext();
	}
	
	public IndexQuery getNextQuery(Object tag) {
		if (hasNext()) {
			IndexQuery query = (IndexQuery) clone();
			query.setTag(tag);
			query.setIdParagraph(getResult().getNextId());
			query.setResult(null);
			return query;
		}
		return null;
	}
	
	@Override
	public Object clone() {
		IndexQuery query = new IndexQuery();
		query.setBibleKey(this.getBibleKey());
		query.setIdParagraph(this.getIdParagraph());
		query.setIndexType(this.getIndexType());
		query.setTag(this.getTag());
		query.setResult(this.getResult());
		return query;
	}
}
