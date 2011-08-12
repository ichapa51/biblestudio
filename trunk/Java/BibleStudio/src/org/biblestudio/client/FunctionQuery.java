package org.biblestudio.client;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Israel Chapa
 * Creation Date: 01/08/2011
 */
public class FunctionQuery extends TagObject {

	private String name;
	private Map<String,Object> params = new HashMap<String,Object>();
	private Object result;
	
	public FunctionQuery() {
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Map<String,Object> getParams() {
		return params;
	}
	
	public void setParam(String paramName, Object value) {
		params.put(paramName, value);
	}
	
	public Object getParam(String paramName) {
		return params.get(paramName);
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Object getResult() {
		return result;
	}
}
