package org.biblestudio.client.db;

import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;
/**
 * @author Israel Chapa
 * Creation Date: 19/07/2011
 */
public interface AddBibleTransaction {
	
	Bible getBible();
	
	void addBook(Book book) throws Exception;
	
	void addBookDiv(BookDiv div) throws Exception;
	
	void addParagraph(Paragraph p) throws Exception;
	
	void commit() throws Exception;
	
	void rollback() throws Exception;
}
