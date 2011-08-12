package org.biblestudio.client;

import org.biblestudio.model.Paragraph;

public class SearchQuery extends PagedQuery<Paragraph> {

	private SearchType searchType = SearchType.Auto;
	private String bibleKey;
	private boolean includingHeaders = false;
	private boolean ignoringAccents = true;
	private boolean quotedCaseSentitive = true;
	private int[] includedBooks;
	private int[] notIncludedBooks;
	private String text;
	
	public SearchQuery() {
		
	}
	
	public SearchQuery(Object tag) {
		super(tag);
	}
	
	public SearchQuery(Object tag, String text) {
		super(tag);
		this.text = text;
	}
	
	public SearchQuery(SearchType type, String text) {
		this.setSearchType(type);
		this.text = text;
	}
	
	public void setBibleKey(String bibleKey) {
		this.bibleKey = bibleKey;
	}

	public String getBibleKey() {
		return bibleKey;
	}

	public void setIncludingHeaders(boolean includingHeaders) {
		this.includingHeaders = includingHeaders;
	}

	public boolean isIncludingHeaders() {
		return includingHeaders;
	}

	public void setIgnoringAccents(boolean ignoringAccents) {
		this.ignoringAccents = ignoringAccents;
	}

	public boolean isIgnoringAccents() {
		return ignoringAccents;
	}

	public void setQuotedCaseSentitive(boolean quotedCaseSentitive) {
		this.quotedCaseSentitive = quotedCaseSentitive;
	}

	public boolean isQuotedCaseSentitive() {
		return quotedCaseSentitive;
	}

	public void setSearchType(SearchType searchType) {
		this.searchType = searchType;
	}

	public SearchType getSearchType() {
		return searchType;
	}

	public void setIncludedBooks(int[] includedBooks) {
		this.includedBooks = includedBooks;
	}

	public int[] getIncludedBooks() {
		return includedBooks;
	}

	public void setNotIncludedBooks(int[] notIncludedBooks) {
		this.notIncludedBooks = notIncludedBooks;
	}

	public int[] getNotIncludedBooks() {
		return notIncludedBooks;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	@Override
	public Object clone() {
		SearchQuery q = new SearchQuery();
		q.setTag(getTag());
		q.setBibleKey(getBibleKey());
		q.setOffset(getOffset());
		q.setMaxRows(getMaxRows());
		q.setResult(getResult());
		q.setSearchType(getSearchType());
		q.setText(getText());
		return q;
	}
}
