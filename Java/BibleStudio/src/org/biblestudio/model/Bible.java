package org.biblestudio.model;

public class Bible extends TitleEntity {

	private String name;
	private boolean isTemplate;
	private Bible template;
	private Integer idTemplate;
	private String lang;
	private Author author;
	private Integer idAuthor;
	private String version;
	private String description;
	private String comments;
	private Integer bookCount;
	private BinaryFile cover;
	private Integer idCover;
	
	public Bible() {
		
	}
	
	public Bible(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setIsTemplate(boolean isTemplate) {
		this.isTemplate = isTemplate;
	}

	public boolean getIsTemplate() {
		return isTemplate;
	}

	public void setTemplate(Bible template) {
		this.template = template;
	}

	public Bible getTemplate() {
		return template;
	}

	public void setIdTemplate(Integer idTemplate) {
		this.idTemplate = idTemplate;
	}

	public Integer getIdTemplate() {
		return idTemplate;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Author getAuthor() {
		return author;
	}

	public void setIdAuthor(Integer idAuthor) {
		this.idAuthor = idAuthor;
	}

	public Integer getIdAuthor() {
		return idAuthor;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getComments() {
		return comments;
	}

	public void setBookCount(Integer bookCount) {
		this.bookCount = bookCount;
	}

	public Integer getBookCount() {
		return bookCount;
	}

	public void setCover(BinaryFile cover) {
		this.cover = cover;
	}

	public BinaryFile getCover() {
		return cover;
	}

	public void setIdCover(Integer idCover) {
		this.idCover = idCover;
	}

	public Integer getIdCover() {
		return idCover;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}
}
