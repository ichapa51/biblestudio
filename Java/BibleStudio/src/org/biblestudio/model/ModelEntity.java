package org.biblestudio.model;

public abstract class ModelEntity {

	private Integer id;
	
	public ModelEntity() {
		
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
