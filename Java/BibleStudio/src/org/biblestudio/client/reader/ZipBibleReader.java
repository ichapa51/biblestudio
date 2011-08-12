package org.biblestudio.client.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.biblestudio.client.BookReader;
import org.biblestudio.model.Bible;
import org.biblestudio.util.FileUtils;

public class ZipBibleReader extends AbstractBibleReader {

	private File inputFile;
	private InputStream inputStream;
	private Iterator<BookReader> books;
	private byte[] data;
	
	public ZipBibleReader(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public ZipBibleReader(File file) {
		this.inputFile = file;
	}
	
	public ZipBibleReader(String fileName) {
		this.inputFile = new File(fileName);
	}
	
	public static boolean isZipSource(File file) {
		String[] exts = getExtensions();
		for (int i = 0; i < exts.length; i++) {
			if (exts[i].equalsIgnoreCase(FileUtils.getFileExtension(file))) {
				return true;
			}
		}
		return false;
	}
	
	public static String[] getExtensions() {
		return new String[] {"zip", "bblz"};
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
	
	protected void loadBooks(boolean setBible) throws Exception {
		Map<String, BookReader> sourceMap = new HashMap<String, BookReader>();
		TreeSet<String> treeSet = new TreeSet<String>();
		ZipInputStream zipStream = new ZipInputStream(getInputStream());
		ZipEntry zipEntry = null;
		Bible bbl = null;
		String fileName = null;
		while((zipEntry = zipStream.getNextEntry()) != null) {
			fileName = zipEntry.getName();
			if (this.isBookFile(fileName)) {
				String fileNameWithoutExt = FileUtils.getFileNameWithoutExt(fileName);
				BookReader source = this.createHtmlBookSource(fileNameWithoutExt,
						FileUtils.createByteArrayInputStream(zipStream, false));
				treeSet.add(fileNameWithoutExt);
				sourceMap.put(fileNameWithoutExt, source);
			} else if (setBible && this.isBibleMetaFile(fileName)) {
				Bible b = new Bible(FileUtils.getFileNameWithoutExt(fileName));
				bbl = this.parseBibleFromXml(b, FileUtils.createByteArrayInputStream(zipStream, false));
			}
		}
		zipStream.close();
		if (bbl != null) {
			setBible(bbl);
		}
		ArrayList<BookReader> sourceList = new ArrayList<BookReader>(sourceMap.size());
		for (String key : treeSet) {
			sourceList.add(sourceMap.get(key));
		}
		if (bible != null) {
			bible.setBookCount(sourceList.size());
			for (BookReader source : sourceList) {
				((HtmlBookReader)source).setBible(bible);
			}
		}
		books = sourceList.iterator();
	}
	
	@Override
	protected Bible loadBible() throws Exception {
		loadBooks(bible == null);
		return bible;
	}

	@Override
	public BookReader getNextBookReader() throws Exception {
		readBible();
		if (books == null) {
			loadBooks(false);
		}
		if (books.hasNext()) {
			return books.next();
		}
		close();
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.BibleReader#close()
	 */
	@Override
	public void close() {
		data = null;
		books = null;
	}
}
