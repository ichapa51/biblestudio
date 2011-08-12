package org.biblestudio.client.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.biblestudio.client.BookReader;
import org.biblestudio.client.DivReader;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.util.FileUtils;
import org.biblestudio.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HtmlBookReader extends AbstractBibleReader implements BookReader {

	private boolean isBookRead = false;
	private Book book;
	private File inputFile;
	private InputStream inputStream;
	private byte[] data;
	private Iterator<Element> divs;
	private String bookTitle;
	
	public HtmlBookReader(File file) {
		this.bookTitle = FileUtils.getFileNameWithoutExt(file);
		this.inputFile = file;
	}
	
	public HtmlBookReader(String bookTitle, InputStream inputStream) {
		this.bookTitle = bookTitle;
		this.inputStream = inputStream;
	}
	
	protected InputStream getInputStream() throws Exception {
		if (data != null) {
			return FileUtils.createInputStream(data);
		}
		if (inputStream != null) {
			data = FileUtils.createByteArray(inputStream, true);
			return FileUtils.createInputStream(data);
		}
		if (inputFile != null) {
			inputStream = new BufferedInputStream(new FileInputStream(inputFile));
			data = FileUtils.createByteArray(inputStream, true);
			return FileUtils.createInputStream(data);
		}
		return inputStream;
	}

	@Override
	public Bible loadBible() throws Exception {
		if (bible == null) {
			bible = readBook().getBible();
		}
		return bible;
	}

	@Override
	public BookReader getNextBookReader() throws Exception {
		if (!isBookRead) {
			isBookRead = true;
			return this;
		}
		return null;
	}

	@Override
	public Book readBook() throws Exception {
		if (book == null) {
			if (data == null) {
				data = FileUtils.createByteArray(getInputStream(), true);
			}
			book = new Book();
			book.setTitle(bookTitle);
			book = this.parseBookFromXml(book, getInputStream());
		}
		return book;
	}

	@Override
	public DivReader getNextDivReader() throws Exception {
		readBook();
		if (divs == null) {
			divs = this.getBookDivNodesFromXml(getInputStream()).iterator();
		}
		if (divs.hasNext()) {
			return new HtmlDivReader(book, divs.next());
		}
		close();
		return null;
	}
	
	protected Book parseBookFromXml(Book book, InputStream input) throws Exception {
		//Document xmlDoc = XmlUtils.parseDocument(input);
		if (book == null) {
			book = new Book();
		}
		String bookName = book.getTitle();
		if (bookName != null) {
			int i = bookName.indexOf('-');
			if (i > 0) {
				try {
					book.setId(Integer.parseInt(bookName.substring(0, i)));
				} catch (Exception e) {
					
				}
				book.setTitle(bookName.substring(i + 1));
			}
		}
		// TODO Parse xml meta
		return book;
	}
	
	protected List<Element> getBookDivNodesFromXml(InputStream input) throws Exception {
		Document xmlDoc = XmlUtils.parseInputDocument(input);
		List<Element> list = new ArrayList<Element>();
		Element html = xmlDoc.getDocumentElement();
		Element body = XmlUtils.getFirstChildEl(html, "body");
		if (body != null) {
			XmlUtils.addChildElements(list, body, "div");
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleReader#close()
	 */
	@Override
	public void close() {
		divs = null;
		data = null;
	}
}
