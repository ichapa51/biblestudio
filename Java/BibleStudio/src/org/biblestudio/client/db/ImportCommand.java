package org.biblestudio.client.db;

import org.biblestudio.client.BibleReader;
import org.biblestudio.client.BibleSource;
import org.biblestudio.client.BookReader;
import org.biblestudio.client.DivReader;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;

public class ImportCommand extends DataCommand<BibleSource> {

	public ImportCommand(BibleDbEngine engine, BibleSource input) {
		super(engine, input);
	}
	
	private void fireProgress(Integer bookCount, int book) {
		if (bookCount != null) {
			this.firePercentDone((book*1.0 / bookCount) * 100);
		}
	}
	
	@Override
	public void execute() {
		boolean success = true;
		AddBibleTransaction trans = null;
		try {
			fireMessageSent("Starting import...");
			BibleSource source = getInput();
			BibleReader bibleSource = source.getReader();
			Bible bible = bibleSource.readBible();
			if (bible == null) {
				throw new Exception("Reader returned a null bible");
			}
			trans = dbEngine.createAddBibleTransaction(bible, source.getImportMode());
			Integer bookCount = bible.getBookCount();
			if (bookCount != null) {
				fireMessageSent("Importing " + bookCount + " books...");
			}
			int realBookCount = 0;
			BookReader bookReader = bibleSource.getNextBookReader();
			while (bookReader != null) {
				realBookCount++;
				Book book = bookReader.readBook();
				trans.addBook(book);
				DivReader divReader = bookReader.getNextDivReader();
				while (divReader != null) {
					BookDiv div = divReader.readBookDiv();
					trans.addBookDiv(div);
					Paragraph para = divReader.getNextParagraph();
					while (para != null) {
						trans.addParagraph(para);
						para = divReader.getNextParagraph();
					}
					divReader = bookReader.getNextDivReader();
				}
				fireMessageSent(book.getTitle() + "(" + realBookCount + ") imported...");
				fireProgress(bookCount, realBookCount);
				bookReader = bibleSource.getNextBookReader();
			}
			fireMessageSent("Commiting...");
			trans.commit();
		} catch (Exception e) {
			success = false;
			fireErrorFound(e);
			try {
				trans.rollback();
			} catch (Exception r) {
				fireErrorFound(r);
			}
		} finally {
			fireActionCompleted(success);
		}
	}
}
