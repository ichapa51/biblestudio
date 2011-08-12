package org.biblestudio.client.reader;

import java.io.File;

import org.biblestudio.client.BibleReader;
import org.biblestudio.client.BookReader;
import org.biblestudio.model.Bible;

public class FileBibleReader extends AbstractBibleReader {

	private BibleReader source;
	
	public FileBibleReader(File file) {
		source = createBibleSourceFromFile(file);
	}
	
	public static BibleReader createBibleSourceFromFile(File file) {
		if (file.isDirectory()) {
			return new FolderBibleReader(file);
		}
		if (ZipBibleReader.isZipSource(file)) {
			return new ZipBibleReader(file);
		}
		return new HtmlBookReader(file);
	}

	@Override
	public Bible readBible() throws Exception {
		return source.readBible();
	}

	@Override
	public BookReader getNextBookReader() throws Exception {
		return source.getNextBookReader();
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleReader#close()
	 */
	@Override
	public void close() {
		source.close();
	}
}
