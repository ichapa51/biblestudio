package org.biblestudio.client;

import org.biblestudio.model.Author;
import org.biblestudio.model.Bible;
import org.biblestudio.model.BinaryFile;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.BookGroup;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 05/08/2011
 */
public class CommonQueries {

	private CommonQueries() {
		
	}
	
	public static IdQuery getAuthorById(int idAuthor, Object tag) {
		return new IdQuery(tag, Author.class, idAuthor);
	}
	
	public static IdQuery getBibleById(int idBible, Object tag) {
		return new IdQuery(tag, Bible.class, idBible);
	}
	
	public static IdQuery getBookById(int idBook, Object tag) {
		return new IdQuery(tag, Book.class, idBook);
	}
	
	public static IdQuery getBookGroupById(int idBookGroup, Object tag) {
		return new IdQuery(tag, BookGroup.class, idBookGroup);
	}
	
	public static IdQuery getBinaryFileById(int idBinaryFile, Object tag) {
		return new IdQuery(tag, BinaryFile.class, idBinaryFile);
	}
	
	public static ModelQuery getAuthors(Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(Author.class);
		return q;
	}
	
	public static ModelQuery getBibles(Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(Bible.class);
		return q;
	}
	
	public static ModelQuery getBibles(boolean isTemplate, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(Bible.class);
		q.addCondition("IsTemplate", "=", isTemplate + "");
		return q;
	}
	
	public static ModelQuery getBooksByBibleId(int idBible, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(Book.class);
		q.addCondition("IdBible", "=", idBible + "");
		return q;
	}
	
	public static ModelQuery getBooksByGroupId(int idBookGroup, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(Book.class);
		q.addCondition("IdGroup", "=", idBookGroup + "");
		return q;
	}
	
	public static ModelQuery getBookGroupsByBibleId(int idBible, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(BookGroup.class);
		q.addCondition("IdBible", "=", idBible + "");
		return q;
	}
	
	public static ModelQuery getBookDivsByBookId(int idBook, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(BookDiv.class);
		q.addCondition("IdBook", "=", idBook + "");
		return q;
	}
	
	public static ModelQuery getBookDivBy(int idBook, String divKey, Object tag) {
		ModelQuery q = new ModelQuery(tag);
		q.setEntityType(BookDiv.class);
		q.addCondition("IdBook", "=", idBook + "");
		q.addCondition("DivKey", "=", divKey);
		return q;
	}
}
