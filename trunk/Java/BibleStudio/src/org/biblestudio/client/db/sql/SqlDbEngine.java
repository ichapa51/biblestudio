package org.biblestudio.client.db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.biblestudio.client.ImportMode;
import org.biblestudio.client.db.AbstractDbEngine;
import org.biblestudio.client.db.AddBibleTransaction;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.Paragraph;
import org.biblestudio.util.StringUtils;

public abstract class SqlDbEngine extends AbstractDbEngine {

	protected Connection sharedConn = null;
	
	public SqlDbEngine() {
		
	}
	
	public abstract String getDriverClassName();
	
	public abstract void addBible(Connection conn, Bible bible, ImportMode mode) throws Exception;
	
	public abstract void addBook(Connection conn, Bible bible, Book book) throws Exception;
	
	public abstract void addBookDiv(Connection conn, Bible bible, BookDiv div) throws Exception;
	
	public abstract void addParagraph(Connection conn, Bible bible, Paragraph p) throws Exception;
	
	public abstract void beforeBibleAdded(Connection conn) throws Exception;
	
	public abstract void afterBibleAdded() throws Exception;

	@Override
	public void start() throws Exception {
		Class.forName(getDriverClassName());
		getSharedConnection();
	}

	@Override
	public void shutdown() throws Exception {
		closeSharedConnections();
		sharedConn = null;
	}
	
	@Override
	public AddBibleTransaction createAddBibleTransaction(Bible bible, ImportMode mode) throws Exception {
		return new SqlAddBibleTransaction(this, bible, mode);
	}
	
	public Connection newConnection(boolean autoCommit) throws Exception {
		String url = this.getConnectionConfig().getUrl();
		String user = this.getConnectionConfig().getUsername();
		String pwd = this.getConnectionConfig().getPassword();
		Connection conn = DriverManager.getConnection(url, user, pwd);
		conn.setAutoCommit(autoCommit);
		return conn;
	}
	
	public Connection getSharedConnection() throws Exception {
		if (sharedConn == null) {
			sharedConn = newConnection(true);
		} else if (sharedConn.isClosed() || !sharedConn.isValid(0)) {
			sharedConn = newConnection(true);
		}
		return sharedConn;
	}
	
	public void closeSharedConnections() throws Exception {
		if (sharedConn != null && !sharedConn.isClosed()) {
			sharedConn.close();
			sharedConn = null;
		}
	}
	
	public Statement createStatement() throws Exception {
		Connection conn = getSharedConnection();
		return conn.createStatement();
	}
	
	public void executeUpdate(String sql) throws Exception {
		executeUpdate(getSharedConnection(), sql);
	}
	
	public void executeUpdate(Connection conn, String sql) throws Exception {
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
	
	public ResultSet executeQuery(String sql) throws Exception {
		return executeQuery(getSharedConnection(), sql);
	}
	
	public ResultSet executeQuery(Connection conn, String sql) throws Exception {
		Statement stmt = conn.createStatement();
		return stmt.executeQuery(sql);
	}
	
	public Object executeScalar(String sql) throws Exception {
		return executeScalar(getSharedConnection(), sql);
	}
	
	public Object executeScalar(Connection conn, String sql) throws Exception {
		ResultSet rs = executeQuery(conn, sql);
		Object result = null;
		if (rs.next()) {
			result = rs.getObject(1);
		}
		rs.close();
		return result;
	}
	
	public Object[] executeRecord(String sql) throws Exception {
		return executeRecord(getSharedConnection(), sql);
	}
	
	public Object[] executeRecord(Connection conn, String sql) throws Exception {
		ResultSet rs = executeQuery(conn, sql);
		Object[] result = null;
		if (rs.next()) {
			result = new Object[rs.getMetaData().getColumnCount()];
			for (int i = 1; i <= result.length; i++) {
				result[i - 1] = rs.getObject(i);
			}
		}
		rs.close();
		return result;
	}
	
	public PreparedStatement prepareStatement(String sql) throws Exception {
		Connection conn = getSharedConnection();
		return conn.prepareStatement(sql);
	}
	
	public String getLikeExpForQuotedString(String s, boolean addPercent) {
		String like = StringUtils.replace(StringUtils.replace(s, "*", "%"), "?", "_");
		if (addPercent) {
			return "'%" + like  + "%'";
		}
		return "'" + like + "'";
	}
}
