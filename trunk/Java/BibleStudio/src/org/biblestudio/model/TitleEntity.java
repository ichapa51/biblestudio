package org.biblestudio.model;

import java.util.ArrayList;
import java.util.List;

public abstract class TitleEntity extends ModelEntity {

	private String title;
	private String longTitle;
	private String abbreviation;
	private List<Title> titles;
	
	public TitleEntity() {
		
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public void setLongTitle(String longTitle) {
		this.longTitle = longTitle;
	}

	public String getLongTitle() {
		return longTitle;
	}

	public void setAbbreviation(String abbrevation) {
		this.abbreviation = abbrevation;
	}

	public String getAbbreviation() {
		if (abbreviation != null && !abbreviation.endsWith(".")) {
			return abbreviation + ".";
		}
		return abbreviation;
	}

	public void addTitle(Title title) {
		if (titles == null) {
			this.titles = new ArrayList<Title>();
		}
		titles.add(title);
	}

	public List<Title> getTitles() {
		return titles;
	}
	
	public void setTitles() {
		if (titles != null && titles.size() > 0) {
			setTitle(titles.get(0).getName());
			for (Title title : titles) {
				if (title.isAbbr()) {
					setAbbreviation(title.getName());
					break;
				}
			}
			for (Title title : titles) {
				if (!title.isAbbr() && title.getName().length() > getTitle().length()) {
					setLongTitle(title.getName());
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return this.getTitle();
	}
}
