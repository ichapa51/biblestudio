package org.biblestudio.client;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 19/07/2011
 */
public class BibleSource extends TagObject {
	
	private ImportMode importMode = ImportMode.REPLACE;
	private BibleReader reader;
	
	public BibleSource() {
		
	}
	
	public BibleSource(BibleReader reader) {
		this.reader = reader;
	}
	
	public BibleSource(BibleReader reader, ImportMode mode) {
		this.reader = reader;
		this.importMode = mode;
	}

	public void setImportMode(ImportMode importMode) {
		this.importMode = importMode;
	}

	public ImportMode getImportMode() {
		return importMode;
	}

	public void setReader(BibleReader reader) {
		this.reader = reader;
	}

	public BibleReader getReader() {
		return reader;
	}
}
