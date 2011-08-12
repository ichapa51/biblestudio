package org.biblestudio.model;

public class BookGroup extends TitleEntity {

	private Bible bible;
	private Integer idBible;
	
	public BookGroup() {
		
	}

	public void setIdBible(Integer idBible) {
		this.idBible = idBible;
	}

	public Integer getIdBible() {
		return idBible;
	}

	public void setBible(Bible bible) {
		this.bible = bible;
	}

	public Bible getBible() {
		return bible;
	}
}
