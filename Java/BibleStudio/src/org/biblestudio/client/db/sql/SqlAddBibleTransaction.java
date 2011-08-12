package org.biblestudio.client.db.sql;

import org.biblestudio.client.ImportMode;
import org.biblestudio.client.db.AddBibleTransaction;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 19/07/2011
 */
public class SqlAddBibleTransaction extends SqlTransaction implements AddBibleTransaction {

	private Bible bible;
	private ImportMode mode;
	
	public SqlAddBibleTransaction(SqlDbEngine engine, Bible bible, ImportMode mode) throws Exception {
		super(engine);
		this.bible = bible;
		this.mode = mode;
		begin();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.AddBibleTransaction#getBible()
	 */
	@Override
	public Bible getBible() {
		return bible;
	}
	
	@Override
	public void begin() throws Exception {
		super.begin();
		engine.addBible(conn, bible, mode);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.AddBibleTransaction#addBook(org.ebible.model.Book)
	 */
	@Override
	public void addBook(Book book) throws Exception {
		engine.addBook(conn, bible, book);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.AddBibleTransaction#addBookDiv(org.ebible.model.BookDiv)
	 */
	@Override
	public void addBookDiv(BookDiv div) throws Exception {
		engine.addBookDiv(conn, bible, div);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.AddBibleTransaction#addParagraph(org.ebible.model.Paragraph)
	 */
	@Override
	public void addParagraph(Paragraph p) throws Exception {
		engine.addParagraph(conn, bible, p);
	}
	
	@Override
	public void commit() throws Exception {
		engine.beforeBibleAdded(conn);
		super.commit();
		engine.afterBibleAdded();
	}
}
