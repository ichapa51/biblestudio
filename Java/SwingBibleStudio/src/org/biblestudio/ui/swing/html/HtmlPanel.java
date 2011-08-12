package org.biblestudio.ui.swing.html;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.biblestudio.model.Paragraph;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 03/08/2011
 */
@SuppressWarnings("serial")
public class HtmlPanel extends JPanel {

	private JScrollPane scrollPane;
	private JEditorPane contentPane;
	
	public HtmlPanel() {
		super(new BorderLayout());
		contentPane = new JEditorPane();
		contentPane.setEditable(false);
		contentPane.setContentType("text/html");
		scrollPane = new JScrollPane(contentPane);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setHtml(String content) {
		contentPane.setText(content);
	}
	
	public void setContent(List<Paragraph> content) {
		String html = "";//"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"; 
		html += "<html xmlns=\"http://www.w3.org/1999/xhtml\">";
		html += "<head><style type=\"text/css\" media=\"all\">body{font-size:14;font-family:Arial;color:black} p{text-align:justify} .xref{color:silver;font-size:x-small;padding:5;} .fnote{color:silver;font-size:x-small;padding:5;}</style></head>";
		html += "<body><div>";
		String pXml = null;
		for (Paragraph p : content) {
			pXml = p.getXmlText();
			int i = pXml.indexOf(">");
			if (i != -1 && p.getVerse() != null) {
				pXml = pXml.substring(0, i + 1) + "<sup style=\"color:silver;\">" + p.getVerse() + "</sup>" + pXml.substring(i + 1);
			}
			html += pXml;
		}
		html += "</div></body>";
		html += "</html>";
		setHtml(html);
	}
}
