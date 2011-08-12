package org.biblestudio.client;

import java.io.File;

import org.biblestudio.client.reader.FileBibleReader;


public class BibleFactory {

	private static BibleFactory factory = new BibleFactory();
	
	protected BibleFactory() {
		
	}
	
	public BibleReader createBibleSourceFromFile(File file) {
		return FileBibleReader.createBibleSourceFromFile(file);
	}
	
	public static BibleFactory getFactory() {
		return factory;
	}
}
