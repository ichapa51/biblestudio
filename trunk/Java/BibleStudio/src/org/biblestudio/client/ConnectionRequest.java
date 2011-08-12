package org.biblestudio.client;

import java.util.Locale;

public class ConnectionRequest extends TagObject {

	private String url;
	private String username;
	private String password;
	private Locale uiLocale;
	private Locale docLocale;
	private ConnectionResponse response;
	
	public ConnectionRequest() {
		uiLocale = Locale.getDefault();
		docLocale = Locale.getDefault();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setUILocale(Locale uiLocale) {
		this.uiLocale = uiLocale;
	}

	public Locale getUILocale() {
		return uiLocale;
	}

	public void setDocLocale(Locale docLocale) {
		this.docLocale = docLocale;
	}

	public Locale getDocLocale() {
		return docLocale;
	}

	public void setResponse(ConnectionResponse response) {
		this.response = response;
	}

	public ConnectionResponse getResponse() {
		return response;
	}
}