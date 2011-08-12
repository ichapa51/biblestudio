package org.biblestudio.model;

public class Book extends TitleEntity {

	private BookType type;
	private Integer idBible;
	private Bible bible;
	private BookGroup group;
	private Integer idGroup;
	private Author author;
	private Integer idAuthor;
	private Integer year;
	private Integer chapters;
	private BinaryFile cover;
	private Integer idCover;
	private Integer divCount;
	
	public Book() {
		
	}

	public void setType(BookType type) {
		this.type = type;
	}

	public BookType getType() {
		return type;
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

	public void setGroup(BookGroup group) {
		this.group = group;
	}

	public BookGroup getGroup() {
		return group;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getYear() {
		return year;
	}

	public void setChapters(Integer chapters) {
		this.chapters = chapters;
	}

	public Integer getChapters() {
		return chapters;
	}

	public void setDivCount(Integer divCount) {
		this.divCount = divCount;
	}

	public Integer getDivCount() {
		return divCount;
	}

	public void setCover(BinaryFile cover) {
		this.cover = cover;
	}

	public BinaryFile getCover() {
		return cover;
	}

	public void setIdGroup(Integer idGroup) {
		this.idGroup = idGroup;
	}

	public Integer getIdGroup() {
		return idGroup;
	}

	public void setIdAuthor(Integer idAuthor) {
		this.idAuthor = idAuthor;
	}

	public Integer getIdAuthor() {
		return idAuthor;
	}

	public void setIdCover(Integer idCover) {
		this.idCover = idCover;
	}

	public Integer getIdCover() {
		return idCover;
	}
}