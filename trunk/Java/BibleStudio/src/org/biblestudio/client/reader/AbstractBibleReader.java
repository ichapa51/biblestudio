package org.biblestudio.client.reader;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.biblestudio.client.BibleReader;
import org.biblestudio.client.BookReader;
import org.biblestudio.client.TagObject;
import org.biblestudio.model.Bible;
import org.biblestudio.util.FileUtils;
import org.biblestudio.util.XmlUtils;

public abstract class AbstractBibleReader extends TagObject implements BibleReader {

	protected Bible bible;
	
	public AbstractBibleReader() {
		
	}

	public void setBible(Bible bible) {
		this.bible = bible;
	}

	@Override
	public Bible readBible() throws Exception {
		if (bible == null) {
			bible = loadBible();
		}
		return bible;
	}
	
	protected Bible loadBible() throws Exception {
		return null;
	}
	
	public boolean isBibleMetaFile(String fileName) {
		String ext = FileUtils.getFileExtension(fileName);
		if ("bbl".equalsIgnoreCase(ext) || "xml".equalsIgnoreCase(ext)) {
			return true;
		}
		return false;
	}
	
	public boolean isBookFile(String fileName) {
		String ext = FileUtils.getFileExtension(fileName);
		if ("html".equalsIgnoreCase(ext) || "htm".equalsIgnoreCase(ext)) {
			return true;
		}
		return false;
	}
	
	protected Bible parseBibleFromXml(Bible bbl, InputStream input) throws Exception {
		Document xmlDoc = XmlUtils.parseInputDocument(input);
		if (bbl == null) {
			bbl = new Bible();
		}
		Element root = xmlDoc.getDocumentElement();
		bbl.setName(XmlUtils.coalesceAttribute(root, "name", bbl.getName()));
		Bible template = new Bible();
		template.setName(root.getAttribute("template"));
		if (template.getName() != null) {
			bbl.setTemplate(template);
		}
		bbl.setLang(XmlUtils.coalesceAttribute(root, "lang", bbl.getLang()));
		bbl.setVersion(XmlUtils.coalesceAttribute(root, "version", bbl.getVersion()));
		bbl.setDescription(XmlUtils.coalesceAttribute(root, "description", bbl.getDescription()));
		// TODO read aliases, comments, books, etc
		return bbl;
	}
	
	protected BookReader createHtmlBookSource(String bookTitle, InputStream input) throws Exception {
		return new HtmlBookReader(bookTitle, input);
	}
}
