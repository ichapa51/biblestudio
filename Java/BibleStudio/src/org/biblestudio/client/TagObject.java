package org.biblestudio.client;

public class TagObject implements Tag {

	private Object tag;
	
	public TagObject() {
		
	}
	
	public TagObject(Object tag) {
		this.tag = tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	@Override
	public Object getTag() {
		return tag;
	}
}
