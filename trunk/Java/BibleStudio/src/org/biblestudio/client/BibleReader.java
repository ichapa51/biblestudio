package org.biblestudio.client;

import org.biblestudio.model.Bible;

public interface BibleReader {
	
	Bible readBible() throws Exception;
	
	BookReader getNextBookReader() throws Exception;
	
	void close();
}
