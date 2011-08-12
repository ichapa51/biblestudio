package org.biblestudio.client;

import org.biblestudio.model.Book;

public interface BookReader {

	Book readBook() throws Exception;
	
	DivReader getNextDivReader() throws Exception;
	
	void close();
}