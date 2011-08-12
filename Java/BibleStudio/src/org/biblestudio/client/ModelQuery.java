package org.biblestudio.client;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.biblestudio.model.ModelEntity;
import org.biblestudio.util.ReflectionUtils;

public class ModelQuery extends TagObject {

	private Class<? extends ModelEntity> entityType;
	private Map<String,QueryCondition> condition; 
	private List<? extends ModelEntity> result;
	
	public ModelQuery() {
		
	}
	
	public ModelQuery(Object tag) {
		super(tag);
	}
	
	public ModelQuery(Class<? extends ModelEntity> entityType) {
		this.entityType = entityType;
	}
	
	public ModelQuery(Class<? extends ModelEntity> entityType, QueryCondition cond) {
		this.entityType = entityType;
		addCondition(cond);
	}

	public void setEntityType(Class<? extends ModelEntity> entityType) {
		this.entityType = entityType;
	}

	public Class<? extends ModelEntity> getEntityType() {
		return entityType;
	}

	public void setResult(List<? extends ModelEntity> result) {
		this.result = result;
	}

	public List<? extends ModelEntity> getResult() {
		return result;
	}
	
	public ModelEntity getFirstResult() {
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
	
	public void addCondition(QueryCondition cond) {
		if (this.condition == null) {
			condition = new Hashtable<String,QueryCondition>();
		}
		condition.put(cond.getField(), cond);
	}
	
	public void addCondition(String field, String operator, String value) {
		addCondition(new QueryCondition(field, operator, value));
	}
	
	public boolean hasCondition() {
		return this.condition != null;
	}

	public void clearCondition() {
		this.condition = null;
	}

	public Map<String,QueryCondition> getCondition() {
		return condition;
	}
	
	public String paramString() {
		return ReflectionUtils.getShortClassName(getEntityType()) + "(" + getCondition() + ")";
	}
	
	@Override
	public String toString() {
		return paramString();
	}
}
