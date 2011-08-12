package org.biblestudio.client;

import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;

public interface DivReader {

	BookDiv readBookDiv() throws Exception;
	
	Paragraph getNextParagraph() throws Exception;
	
	void close();
}
