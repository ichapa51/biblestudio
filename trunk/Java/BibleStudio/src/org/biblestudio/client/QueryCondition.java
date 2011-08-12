package org.biblestudio.client;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class QueryCondition {

	private String field;
	private Operator operator;
	private String value;
	
	public QueryCondition() {
		
	}
	
	public QueryCondition(String field, Operator operator, String value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
	
	public QueryCondition(String field, String operator, String value) {
		this.field = field;
		this.operator = Operator.getOperator(operator);
		this.value = value;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getField() + " " + getOperator() + " " + getValue();
	}
}
