package org.biblestudio.util;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class EntityField {

	private Class<?> type;
	private String name;
	
	public EntityField() {
		
	}
	
	public EntityField(Class<?> type, String name) {
		this.type = type;
		this.name = name;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Class<?> getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public boolean isPrimitive() {
		return type.isPrimitive();
	}
	
	public boolean isEnum() {
		return type.isEnum();
	}
	
	public boolean isObject() {
		return !isPrimitive() && !isEnum();
	}
	
	public boolean isCompatible(Class<?> parent) {
		return type.isAssignableFrom(parent);
	}
	
	public boolean isString() {
		return String.class.equals(type);
	}
	
	public boolean isInteger() {
		return Integer.class.equals(type) || int.class.equals(type);
	}
	
	public boolean isBoolean() {
		return Boolean.class.equals(type) || boolean.class.equals(type);
	}
	
	public boolean isDouble() {
		return Double.class.equals(type) || double.class.equals(type);
	}
}
