package org.biblestudio.ui.swing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Properties;

import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.util.FileUtils;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 29/07/2011
 */
public class AppConfig {
	
	private static final String FILE = System.getProperty("user.home") + FileUtils.FILE_SEP + ".biblestudio.properties";
	private Properties settings = new Properties();
	
	public AppConfig() {
		load();
	}
	
	protected void load() {
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(FILE));
			settings.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Locale getUILocale() {
		String locale = settings.getProperty("UILocale");
		if (locale == null) {
			locale = Locale.getDefault().toString();
		}
		return new Locale(locale);
	}
	
	public void setUILocale(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		settings.setProperty("UILocale", locale.toString());
		Locale.setDefault(locale);
	}
	
	public Locale getDocLocale() {
		String locale = settings.getProperty("DocLocale");
		if (locale == null) {
			locale = Locale.getDefault().toString();
		}
		return new Locale(locale);
	}
	
	public void setDocLocale(Locale locale) {
		if (locale == null) {
			locale = Locale.getDefault();
		}
		settings.setProperty("DocLocale", locale.toString());
	}
	
	public String getDataClientImpl() {
		return "org.biblestudio.db.hsql.HsqlDataClient";
	}
	
	public ConnectionRequest getConnectionConfig() {
		return null; // TODO
	}
	
	public void save() {
		try {
			settings.setProperty("UILocale", getUILocale().toString());
			settings.setProperty("DocLocale", getDocLocale().toString());
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(FILE));
			settings.store(out, "eBible User Settings");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
