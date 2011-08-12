package org.biblestudio.client;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 20/07/2011
 */
public abstract class PagedQuery<E> extends TagObject {
	
	private Integer offset;
	private Integer maxRows;
	private PageResult<E> result;
	
	public PagedQuery() {
		
	}
	
	public PagedQuery(Object tag) {
		super(tag);
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setMaxRows(Integer maxRows) {
		this.maxRows = maxRows;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public void setResult(PageResult<E> result) {
		this.result = result;
	}

	public PageResult<E> getResult() {
		return result;
	}
}
