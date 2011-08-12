package org.biblestudio.model;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
public class Title {

	private String lang;
	private String name;
	private boolean isAbbr;
	private int priority;
	
	public Title() {
		
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLang() {
		return lang;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAbbr(boolean isAbbr) {
		this.isAbbr = isAbbr;
	}

	public boolean isAbbr() {
		return isAbbr;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}
}