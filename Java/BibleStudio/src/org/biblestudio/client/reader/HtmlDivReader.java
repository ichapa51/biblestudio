package org.biblestudio.client.reader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.biblestudio.client.DivReader;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;
import org.biblestudio.util.StringUtils;
import org.biblestudio.util.XmlUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class HtmlDivReader implements DivReader {

	private Book book;
	private BookDiv div;
	private Element divEl;
	private Iterator<Element> paras;
	
	public HtmlDivReader(Book book, Element divNode) {
		this.book = book;
		this.divEl = divNode;
	}
	
	@Override
	public BookDiv readBookDiv() throws Exception {
		if (div == null) {
			div = parseBookDiv(divEl);
		}
		return div;
	}

	@Override
	public Paragraph getNextParagraph() throws Exception {
		readBookDiv();
		if (paras == null) {
			paras = XmlUtils.addChildElements(null, divEl, "p", "h1", "h2", "h3", "h4", "h5", "h6").iterator();
		}
		if (paras.hasNext()) {
			return parseParagraph(paras.next());
		}
		close();
		return null;
	}
	
	protected BookDiv parseBookDiv(Element divElement) {
		BookDiv bookDiv = new BookDiv();
		bookDiv.setBook(book);
		bookDiv.setDivKey(divElement.getAttribute("id"));
		// TODO Auto-generated method stub
		return bookDiv;
	}
	
	protected Paragraph parseParagraph(Element pEl) {
		Paragraph p = new Paragraph();
		p.setIdBook(book.getId());
		p.setDivKey(div.getDivKey());
		p.setHeader(pEl.getTagName().toLowerCase().startsWith("h"));
		String verse = pEl.getAttribute("id");
		if (!p.isHeader() && verse != null) {
			int k = verse.lastIndexOf('.');
			if (k != -1) {
				verse = verse.substring(k + 1);
			}
			p.setVerse(Integer.parseInt(verse));
		}
		p.setXmlText(StringUtils.trimAll(XmlUtils.getOuterXml(pEl)));
		NodeList spanNodes = pEl.getElementsByTagName("span");
		if (spanNodes.getLength() > 0) {
			List<Element> removeElems = new ArrayList<Element>();
			for (int i = 0; i < spanNodes.getLength(); i++) {
				Element spanEl = (Element)spanNodes.item(i);
				String attr = spanEl.getAttribute("class").toLowerCase();
				if ("fnote".equals(attr) || "xref".equals(attr)) {
					removeElems.add(spanEl);
				}
			}
			for (Element el : removeElems) {
				pEl.removeChild(el);
			}
		}
		p.setPlainText(StringUtils.trimAll(XmlUtils.stripAllTags(XmlUtils.getOuterXml(pEl))));
		// TODO More things?
		return p;
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.DivReader#close()
	 */
	@Override
	public void close() {
		paras = null;
	}
}
