package org.biblestudio.model;

public class BookDiv extends ModelEntity {

	private Book book;
	private Integer idBook;
	private String divKey;
	private String title;
	private Integer paragraphCount;
	
	public BookDiv() {
		
	}

	public void setIdBook(Integer idBook) {
		this.idBook = idBook;
	}

	public Integer getIdBook() {
		return idBook;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Book getBook() {
		return book;
	}
	
	public void setDivKey(String key) {
		this.divKey = key;
	}

	public String getDivKey() {
		return divKey;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setParagraphCount(Integer paragraphCount) {
		this.paragraphCount = paragraphCount;
	}

	public Integer getParagraphCount() {
		return paragraphCount;
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}
}
