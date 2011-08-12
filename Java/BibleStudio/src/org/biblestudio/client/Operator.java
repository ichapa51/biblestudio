package org.biblestudio.client;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public enum Operator {

	EQ("="),
	GT(">"),
	LT("<"),
	NEQ("<>");
	
	private String text;
	
	Operator(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	@Override
	public String toString() {
		return getText();
	}
	
	public static Operator getOperator(String text) {
		for (Operator op : Operator.values()) {
			if (op.getText().equals(text)) {
				return op;
			}
		}
		return null;
	}
}
