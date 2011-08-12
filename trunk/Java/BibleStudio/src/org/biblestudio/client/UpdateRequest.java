package org.biblestudio.client;

import org.biblestudio.model.ModelEntity;

public class UpdateRequest extends TagObject {

	private ModelEntity object;
	
	public UpdateRequest() {
		
	}

	public void setObject(ModelEntity object) {
		this.object = object;
	}

	public ModelEntity getObject() {
		return object;
	}
}
