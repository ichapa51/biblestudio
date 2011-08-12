package org.biblestudio.client;

import java.util.List;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 20/07/2011
 */
public class PageResult<E> {
	
	private int total = 0;
	private List<E> list;
	private PagedQuery<E> previous;
	private PagedQuery<E> next;
	
	public PageResult() {
		
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public List<E> getList() {
		return list;
	}

	public void setPrevious(PagedQuery<E> previous) {
		this.previous = previous;
	}

	public PagedQuery<E> getPrevious() {
		return previous;
	}

	public void setNext(PagedQuery<E> next) {
		this.next = next;
	}

	public PagedQuery<E> getNext() {
		return next;
	}
}
