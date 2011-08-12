package org.biblestudio.client;

import org.biblestudio.model.ModelEntity;
import org.biblestudio.util.ReflectionUtils;

public class IdQuery extends TagObject {

	private Class<? extends ModelEntity> entityType;
	private int id;
	private ModelEntity result;
	
	public IdQuery() {
		
	}
	
	public IdQuery(Class<? extends ModelEntity> entityType) {
		this.entityType = entityType;
	}
	
	public IdQuery(Class<? extends ModelEntity> entityType, int id) {
		this.entityType = entityType;
		this.id = id;
	}
	
	public IdQuery(Object tag, Class<? extends ModelEntity> entityType, int id) {
		super(tag);
		this.entityType = entityType;
		this.id = id;
	}

	public void setEntityType(Class<? extends ModelEntity> entityType) {
		this.entityType = entityType;
	}

	public Class<? extends ModelEntity> getEntityType() {
		return entityType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setResult(ModelEntity result) {
		this.result = result;
	}

	public ModelEntity getResult() {
		return result;
	}
	
	public String paramString() {
		return ReflectionUtils.getShortClassName(getEntityType()) + "(" + getId() + ")";
	}
	
	@Override
	public String toString() {
		return paramString();
	}
}
