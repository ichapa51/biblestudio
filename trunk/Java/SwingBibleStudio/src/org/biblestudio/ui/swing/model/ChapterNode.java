package org.biblestudio.ui.swing.model;

import org.biblestudio.model.Book;
import org.biblestudio.model.ModelEntity;
/**
 * 
 * @author Israel Chapa
 * @since 13/09/2011
 */
public class ChapterNode extends ModelEntity {

	Book book;
	int chapter;
	
	public ChapterNode(Book book, int chapter) {
		this.book = book;
		this.chapter = chapter;
	}
	
	public Book getBook() {
		return book;
	}
	
	public int getChapter() {
		return chapter;
	}
	
	@Override
	public String toString() {
		return chapter + "";
	}
}
