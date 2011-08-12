package org.biblestudio.client;

import java.util.List;
import org.biblestudio.model.Bible;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 11/08/2011
 */
public class ConnectionResponse {

	private List<Bible> bibles;
	
	public ConnectionResponse() {
		
	}

	public void setBibles(List<Bible> bibles) {
		this.bibles = bibles;
	}

	public List<Bible> getBibles() {
		return bibles;
	}
}
