package org.biblestudio.client.db.sql;

import java.sql.Connection;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 19/07/2011
 */
public class SqlTransaction {

	protected SqlDbEngine engine;
	protected Connection conn;
	
	public SqlTransaction(SqlDbEngine engine) {
		this.engine = engine;
	}
	
	public SqlDbEngine getEngine() {
		return this.engine;
	}
	
	public Connection getConnection() {
		if (this.conn == null) {
			throw new IllegalStateException("Transaction must begin first");
		}
		return this.conn;
	}
	
	public void begin() throws Exception {
		this.conn = engine.newConnection(false);
	}
	
	public void commit() throws Exception {
		this.conn.commit();
		this.conn.close();
	}
	
	public void rollback() throws Exception {
		this.conn.rollback();
		this.conn.close();
	}
}
