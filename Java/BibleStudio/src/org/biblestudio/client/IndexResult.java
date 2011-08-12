package org.biblestudio.client;

import java.util.List;

import org.biblestudio.model.Paragraph;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 10/08/2011
 */
public class IndexResult {

	private Integer previousId;
	private Integer nextId;
	private List<Paragraph> list;
	
	public IndexResult() {
		
	}

	public void setList(List<Paragraph> list) {
		this.list = list;
	}

	public List<Paragraph> getList() {
		return list;
	}

	public void setPreviousId(Integer previousId) {
		this.previousId = previousId;
	}

	public Integer getPreviousId() {
		return previousId;
	}

	public void setNextId(Integer nextId) {
		this.nextId = nextId;
	}

	public Integer getNextId() {
		return nextId;
	}
	
	public boolean hasNext() {
		return getNextId() != null;
	}
	
	public boolean hasPrevious() {
		return getPreviousId() != null;
	}
}
