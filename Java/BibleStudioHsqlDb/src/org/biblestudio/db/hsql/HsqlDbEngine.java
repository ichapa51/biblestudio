package org.biblestudio.db.hsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.biblestudio.client.CommonQueries;
import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.client.ConnectionResponse;
import org.biblestudio.client.ImportMode;
import org.biblestudio.client.IndexQuery;
import org.biblestudio.client.IndexResult;
import org.biblestudio.client.IndexType;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.Operator;
import org.biblestudio.client.PageResult;
import org.biblestudio.client.QueryCondition;
import org.biblestudio.client.SearchExp;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.SearchExp.KeywordToken;
import org.biblestudio.client.db.sql.SqlDbEngine;
import org.biblestudio.client.db.sql.SqlTransaction;
import org.biblestudio.model.Bible;
import org.biblestudio.model.Book;
import org.biblestudio.model.BookDiv;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.model.Paragraph;
import org.biblestudio.model.Title;
import org.biblestudio.model.TitleEntity;
import org.biblestudio.util.EntityField;
import org.biblestudio.util.ReflectionUtils;
import org.biblestudio.util.StringUtils;

public class HsqlDbEngine extends SqlDbEngine {

	private static final String SCRIPT = "Script.sql";
	PreparedStatement insertParagraphStmt = null;
	PreparedStatement insertWordStmt = null;
	
	public HsqlDbEngine() {
		
	}

	@Override
	public String getName() {
		return "HSQLDB";
	}
	
	@Override
	public String getDriverClassName() {
		return "org.hsqldb.jdbc.JDBCDriver";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void start() throws Exception {
		super.start();
		checkFirstRun();
		ModelQuery q = CommonQueries.getBibles(null);
		this.modelQuery(q);
		ConnectionResponse response = new ConnectionResponse();
		response.setBibles((List<Bible>)q.getResult());
		this.getConnectionConfig().setResponse(response);
	}

	@Override
	public void shutdown() throws Exception {
		this.executeUpdate("SHUTDOWN");
		super.shutdown();
	}

	@Override
	public ConnectionRequest getDefaultConnectionConfig() {
		ConnectionRequest config = new ConnectionRequest();
		config.setUrl("jdbc:hsqldb:file:" + System.getProperty("user.dir") + "/db/eBible");
		config.setUsername("SA");
		config.setPassword(StringUtils.EMPTY);
		return config;
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#addBible(java.sql.Connection, org.ebible.model.Bible)
	 */
	@Override
	public void addBible(Connection conn, Bible bible, ImportMode mode) throws Exception {
		// Check if exists first
		String bibleTable = bible.getName();
		String wordTable = getWordTable(bibleTable);
		boolean tableExists = false;
		if (ImportMode.REPLACE.equals(mode)) {
			deleteBible(bible, conn);
			addBible(bible, conn);
		} else {
			tableExists = this.tableExists(bibleTable, conn);
		}
		if (!tableExists) {
			executeUpdate(conn, "CREATE CACHED TABLE " + bibleTable + " (Id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1) PRIMARY KEY, IdBook INTEGER NOT NULL, DivKey VARCHAR(20) NOT NULL, Chapter INTEGER NULL, Verse INTEGER NULL, Page INTEGER NULL, IsHeader BOOLEAN NOT NULL, XmlText LONGVARCHAR NOT NULL, PlainText LONGVARCHAR)");
			executeUpdate(conn, "CREATE CACHED TABLE " + wordTable + " (Word VARCHAR(45), Id INTEGER, WordCount INTEGER NOT NULL, PRIMARY KEY(Word, Id))");
		}
		String sql = "INSERT INTO " + bibleTable + " (IdBook,DivKey,Chapter,Verse,Page,IsHeader,XmlText,PlainText) VALUES (?,?,?,?,?,?,?,?)";
		insertParagraphStmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		sql = "INSERT INTO " + wordTable + " (Word, Id, WordCount) VALUES (?, ?, ?)";
		insertWordStmt = conn.prepareStatement(sql);
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#addBook(java.sql.Connection, org.ebible.model.Book)
	 */
	@Override
	public void addBook(Connection conn, Bible bible, Book book) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#addBookDiv(java.sql.Connection, org.ebible.model.BookDiv)
	 */
	@Override
	public void addBookDiv(Connection conn, Bible bible, BookDiv div) throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#addParagraph(java.sql.Connection, org.ebible.model.Paragraph)
	 */
	@Override
	public void addParagraph(Connection conn, Bible bible, Paragraph p) throws Exception {
		insertParagraphStmt.setInt(1, p.getIdBook());
		insertParagraphStmt.setString(2, p.getDivKey());
		if (p.getChapter() != null) {
			insertParagraphStmt.setInt(3, p.getChapter());
		} else {
			if (StringUtils.isIntegerNumber(p.getDivKey())) {
				insertParagraphStmt.setInt(3, Integer.parseInt(p.getDivKey()));
			} else {
				insertParagraphStmt.setNull(3, java.sql.Types.INTEGER);
			}
		}
		if (p.getVerse() != null) {
			insertParagraphStmt.setInt(4, p.getVerse());
		} else {
			insertParagraphStmt.setNull(4, java.sql.Types.INTEGER);
		}
		if (p.getPage() != null) {
			insertParagraphStmt.setInt(5, p.getPage());
		} else {
			insertParagraphStmt.setNull(5, java.sql.Types.INTEGER);
		}
		insertParagraphStmt.setBoolean(6, p.isHeader());
		insertParagraphStmt.setString(7, p.getXmlText());
		insertParagraphStmt.setString(8, p.getPlainText());
		insertParagraphStmt.execute();
		ResultSet rs = insertParagraphStmt.getGeneratedKeys();
		rs.next();
		int id = rs.getInt(1);
		Map<String,Integer> wordMap = this.getIndexWords(bible.getLang(), p.getPlainText());
		for (String word : wordMap.keySet()) {
			insertWordStmt.setString(1, word);
			insertWordStmt.setInt(2, id);
			insertWordStmt.setInt(3, wordMap.get(word));
			insertWordStmt.execute();
		}
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#endBibleImport()
	 */
	@Override
	public void beforeBibleAdded(Connection conn) throws Exception {
		if (insertParagraphStmt != null && !insertParagraphStmt.isClosed()) {
			insertParagraphStmt.close();
		}
		if (insertWordStmt != null && !insertWordStmt.isClosed()) {
			insertWordStmt.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.ebible.client.db.sql.SqlDbEngine#afterBibleAdded()
	 */
	@Override
	public void afterBibleAdded() throws Exception {
		this.executeUpdate("SHUTDOWN COMPACT");
		sharedConn = null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected List<ModelEntity> getEntityList(Class<? extends ModelEntity> entityType, Map<String,QueryCondition> cond) throws Exception {
		EntityField[] fields = ReflectionUtils.getDeclaredSetFields(entityType, true);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Id");
		String fieldName = null;
		for (int i = 0; i < fields.length; i++) {
			fieldName = fields[i].getName();
			if (!fieldName.endsWith("Count")) {
				sql.append(",").append(fieldName);
			}
		}
		sql.append(" FROM ");
		sql.append(ReflectionUtils.getShortClassName(entityType));
		if (cond != null && cond.size() > 0) {
			String[] conds = cond.keySet().toArray(new String[cond.size()]);
			sql.append(" WHERE ");
			QueryCondition qc = cond.get(conds[0]);
			fieldName = qc.getField();
			if (fieldName.equalsIgnoreCase("IDBIBLE") && qc.getOperator().equals(Operator.EQ)) {
				Object idTemplate = executeScalar("SELECT IdTemplate FROM Bible WHERE Id=" + qc.getValue());
				sql.append(fieldName).append(" IN (").append(qc.getValue());
				if (idTemplate != null) {
					sql.append(",").append(idTemplate);
				}
				sql.append(")");
			} else {
				sql.append(fieldName).append(qc.getOperator()).append(qc.getValue());
			}
			for (int i = 1; i < conds.length; i++) {
				qc = cond.get(conds[i]);
				fieldName = qc.getField();
				sql.append(" AND ");
				if (fieldName.equalsIgnoreCase("IDBIBLE") && qc.getOperator().equals(Operator.EQ)) {
					Object idTemplate = executeScalar("SELECT IdTemplate FROM Bible WHERE Id=" + qc.getValue());
					sql.append(fieldName).append(" IN (").append(qc.getValue());
					if (idTemplate != null) {
						sql.append(",").append(idTemplate);
					}
					sql.append(")");
				} else {
					sql.append(fieldName).append(qc.getOperator()).append(qc.getValue());
				}
			}
		}
		String query = sql.toString();
		System.out.println(query);
		ResultSet rs = this.executeQuery(query);
		ModelEntity entity = null;
		List<ModelEntity> list = new ArrayList<ModelEntity>();
		EntityField field = null;
		Object value = null;
		while (rs.next()) {
			entity = entityType.newInstance();
			entity.setId(rs.getInt("Id"));
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];
				if (!field.getName().endsWith("Count")) {
					value = rs.getObject(field.getName());
					ReflectionUtils.setBeanValue(entityType, field.getType(), field.getName(), entity, value);
				}
			}
			list.add(entity);
		}
		rs.close();
		if (entity instanceof TitleEntity) {
			fillTitles((Class<? extends TitleEntity>)entityType, list);
		}
		return list;
	}
	
	@Override
	protected void searchPassage(SearchQuery query, SearchExp.Passage passage) throws Exception {
		SearchExp.BookPsg[] passages = passage.getPassages();
		if (passages == null || passages.length == 0) {
			return;
		}
		String bibleKey = passage.getIdBible();
		if (bibleKey == null) {
			bibleKey = query.getBibleKey();
			if (bibleKey == null) {
				Bible b = getDefaultBible();
				if (b == null) {
					throw new Exception("No bible selected");
				}
				bibleKey = b.getName();
			}
		}
		SearchExp.BookIndex[] indexes = null;
		SearchExp.BookPsg bookPsg = null;
		SearchExp.BookIndex index = null;
		SearchExp.IntRange[] ranges = null;
		SearchExp.IntRange range = null;
		String idBook = null, rangeField = null;
		String sql = "SELECT b.Id, b.IdBook, b.DivKey, b.Chapter, b.IsHeader, b.XmlText, b.PlainText, b.Verse, b.Page ";
		sql += "FROM " + bibleKey + " b WHERE ";
		for (int i = 0; i < passages.length; i++) {
			if (i > 0) {
				sql += "OR ";
			}
			bookPsg = passages[i];
			idBook = bookPsg.getIdBook(); //TODO Find real book
			if (idBook == null) {
				return; // Nothing to do here
			}
			if (!StringUtils.isIntegerNumber(idBook)) {
				Integer id = this.getIdFromAlias(Book.class, idBook, null);
				if (id == null) {
					String[] words = idBook.split(" ");
					if (words.length > 1 && StringUtils.isIntegerNumber(words[words.length -1])) {
						String alias = idBook.substring(0, idBook.lastIndexOf(" ")).trim();
						id = this.getIdFromAlias(Book.class, alias, null);
						if (id != null) {
							index = new SearchExp.BookIndex(false, words[words.length -1]);
							if (bookPsg.hasIndexes()) {
								indexes = new SearchExp.BookIndex[bookPsg.getIndexes().length + 1];
								indexes[0] = index;
								for (int k = 0; k < bookPsg.getIndexes().length; k++) {
									indexes[k + 1] = bookPsg.getIndexes()[k];
								}
							} else {
								indexes = new SearchExp.BookIndex[] {index};
							}
							bookPsg = new SearchExp.BookPsg(alias, indexes);
						}
					}
					if (id == null) {
						return; // Book not found
					}
				}
				idBook = id.toString();
			}
			sql += "(b.IdBook = " + idBook;
			if (bookPsg.hasIndexes()) {
				sql += " AND (";
				indexes = bookPsg.getIndexes();
				for (int j = 0; j < indexes.length; j++) {
					index = indexes[j];
					ranges = index.getIntRange();
					rangeField =  index.isPageIndex() ? "b.Page" : "b.Verse";
					if (j > 0) {
						sql += " OR ";
					}
					sql += "(";
					if (!index.isPageIndex() && index.getDivKey() != null) {
						sql += "b.DivKey = '" + index.getDivKey() + "'";
					}
					if (index.hasIntRange()) {
						sql += " AND ";
						if (ranges.length > 1) {
							sql += "(";
						}
						for (int k = 0; k < ranges.length; k++) {
							range = ranges[k];
							if (k > 0) {
								sql += " OR ";
							}
							if (range.hasRightInt()) {
								sql += "(" + rangeField + " BETWEEN " + range.getLeftInt();
								sql += " AND " + range.getRightInt() + ")";
							} else {
								sql += rangeField + " = " + range.getLeftInt();
							}
						}
						if (ranges.length > 1) {
							sql += ")";
						}
					}
					sql += ")";
				}
				sql += ")";
			}
			sql += ") ";
		}
		if (!query.isIncludingHeaders()) {
			sql += "AND b.IsHeader = FALSE ";
		}
		//TODO Pagination
		System.out.println(sql);
		PageResult<Paragraph> result = new PageResult<Paragraph>();
		List<Paragraph> list = new ArrayList<Paragraph>();
		Statement stmt = this.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		fillParagraphList(list, rs);
		result.setList(list);
		query.setResult(result);
	}
	
	@Override
	protected void searchKeyword(SearchQuery query, SearchExp.Keyword keyword) throws Exception {
		SearchExp.KeywordToken[] tokens = keyword.getTokens();
		if (tokens == null || tokens.length == 0) {
			return;
		}
		String bibleKey = keyword.getIdBible();
		if (bibleKey == null) {
			bibleKey = query.getBibleKey();
			if (bibleKey == null) {
				Bible b = getDefaultBible();
				if (b == null) {
					throw new Exception("No bible selected");
				}
				bibleKey = b.getName();
			}
		}
		String sql = "SELECT b.Id, b.IdBook, b.DivKey, b.Chapter, b.IsHeader, b.XmlText, b.PlainText, b.Verse, b.Page ";
		sql += "FROM " + bibleKey + " b WHERE ";
		SearchExp.KeywordToken token = null;
		String text;
		for (int i = 0; i < tokens.length; i++) {
			token = tokens[i];
			text = token.getText();
			if (SearchExp.KeywordType.Operator.equals(token.getType())) {
				if (KeywordToken.OR.equals(text)) {
					sql += " OR ";
				} else {
					sql += " AND ";
				}
			} else
			if (SearchExp.KeywordType.Word.equals(token.getType())) {
				sql += "b.Id IN (SELECT w.id FROM " + getWordTable(bibleKey) + " w WHERE w.Word LIKE ";
				String searchWord = text.toLowerCase();
				if (query.isIgnoringAccents()) {
					String[] words = StringUtils.getLowerAccentWords(searchWord);
					sql += getLikeExpForQuotedString(words[0], false);
					for (int j = 1; j < words.length; j++) {
						sql += " OR w.Word LIKE ";
						sql += getLikeExpForQuotedString(words[j], false);
					}
				} else {
					sql += getLikeExpForQuotedString(searchWord, false);
				}
				sql += ") ";
			} else { // must be quoted word
				sql += "b.PlainText LIKE "+ getLikeExpForQuotedString(text, true) + " ";
			}
		}
		if (!query.isIncludingHeaders()) {
			sql += "AND b.IsHeader = FALSE ";
		}
		//TODO Pagination
		System.out.println(sql);
		PageResult<Paragraph> result = new PageResult<Paragraph>();
		List<Paragraph> list = new ArrayList<Paragraph>();
		ResultSet rs = executeQuery(sql);
		fillParagraphList(list, rs);
		result.setList(list);
		query.setResult(result);
	}
	
	@Override
	protected void searchAuto(SearchQuery query, SearchExp.Passage passage, SearchExp.Keyword keyword) throws Exception {
		searchPassage(query, passage);
		if (query.getResult() == null || query.getResult().getList() == null ||
				query.getResult().getList().size() == 0) {
			searchKeyword(query, keyword);
		}
	}
	
	@Override
	public void indexQuery(IndexQuery query) throws Exception {
		IndexResult result = new IndexResult();
		String bibleKey = query.getBibleKey();
		if (bibleKey == null) {
			Bible b = getDefaultBible();
			if (b == null) {
				throw new Exception("No bible selected");
			}
			bibleKey = b.getName();
		}
		String bibleTable = bibleKey; //TODO get bible in case is INTEGER
		int pId = query.getIdParagraph();
		IndexType iType = query.getIndexType();
		Object[] p = getIndexObjectForId(bibleTable, iType, pId, "=");
		if (p == null) { // Id does not exist
			result.setList(new ArrayList<Paragraph>());
			query.setResult(result);
			return;
		}
		Integer minId, maxId;
		minId = getLimitIndexQuery(true, pId, bibleTable, iType, p[0], p[1]);
		if (minId != null) {
			result.setPreviousId(minId);
		}
		maxId = getLimitIndexQuery(false, pId, bibleTable, iType, p[0], p[1]);
		if (maxId != null) {
			result.setNextId(maxId);
		}
		String sql = "SELECT b.Id, b.IdBook, b.DivKey, b.Chapter, b.IsHeader, b.XmlText, b.PlainText, b.Verse, b.Page ";
		sql += " FROM " + bibleTable + " b WHERE ";
		if (minId != null) {
			sql += " b.Id > " + minId;
		}
		if (maxId != null) {
			if (minId != null) {
				sql += " AND ";
			}
			sql += " b.Id < " + maxId;
		}
		System.out.println(sql);
		ResultSet rs = executeQuery(sql);
		List<Paragraph> list = new ArrayList<Paragraph>();
		fillParagraphList(list, rs);
		result.setList(list);
		query.setResult(result);
	}
	
	protected void fillParagraphList(List<Paragraph> list, ResultSet rs) throws Exception {
		Paragraph p = null;
		int test = 0;
		while (rs.next()) {
			p = new Paragraph();
			p.setId(rs.getInt("Id"));
			p.setIdBook(rs.getInt("IdBook"));
			p.setDivKey(rs.getString("DivKey"));
			p.setHeader(rs.getBoolean("IsHeader"));
			p.setXmlText(rs.getString("XmlText"));
			p.setPlainText(rs.getString("PlainText"));
			test = rs.getInt("Chapter");
			if (!rs.wasNull()) {
				p.setChapter(test);
			}
			test = rs.getInt("Verse");
			if (!rs.wasNull()) {
				p.setVerse(test);
			}
			test = rs.getInt("Page");
			if (!rs.wasNull()) {
				p.setPage(test);
			}
			list.add(p);
		}
	}
	
	protected Object[] getIndexObjectForId(String bibleTable, IndexType iType, int pId, String operator) throws Exception {
		String sql = "SELECT IdBook, ";
		switch (iType) {
			case BookDiv:
				sql += "DivKey";
				break;
			case Chapter:
				sql += "Chapter";
				break;
			case Page:
				sql += "Page";
				break;
			case Header:
				sql += "IsHeader";
				break;
			case Paragraph:
				sql += "Id";
				break;
		}
		sql += " FROM " + bibleTable + " WHERE Id " + operator + " " + pId;
		System.out.println(sql);
		return executeRecord(sql);
	}
	
	protected Integer getLimitIndexQuery(boolean isMin, int pId, String bibleTable, IndexType iType, Object idBook, Object p) throws Exception {
		String aggr, op;
		if (isMin) {
			aggr = "MAX";
			op = "< ";
		} else {
			aggr = "MIN";
			op = "> ";
		}
		String sql = "SELECT " + aggr + "(Id) FROM " + bibleTable + " WHERE Id ";
		sql += op + " " + pId + " AND (";
		String value = null;
		switch (iType) {
			case BookDiv:
				sql += "DivKey";
				value = "'" + p + "' OR IdBook <> " + idBook;
				break;
			case Chapter:
				sql += "Chapter";
				value = p + "" + "' OR IdBook <> " + idBook;
				break;
			case Page:
				sql += "Page";
				value = p + "";
				break;
			case Header:
				sql += "IsHeader";
				value = p + "";
				break;
			case Paragraph:
				sql += "Id";
				value = p + "";
				break;
		}
		sql += " <> " + value + ")";
		System.out.println(sql);
		return (Integer) executeScalar(sql);
	}
	
	protected void fillTitles(Class<? extends TitleEntity> entityType, List<ModelEntity> list) throws Exception {
		Connection conn = getSharedConnection();
		String tableName = ReflectionUtils.getShortClassName(entityType) + "Alias";
		String sql = "SELECT Name,IsAbbr,Priority,Lang FROM " + tableName + " WHERE (Lang IS NULL OR Lang = '" + getDocLocale().getLanguage() + "') AND Id = ? ORDER BY Lang NULLS LAST, Priority";
		System.out.println(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		TitleEntity te = null;
		Title title = null;
		ResultSet rs = null;
		for (ModelEntity e : list) {
			te = (TitleEntity) e;
			stmt.setInt(1, te.getId());
			rs = stmt.executeQuery();
			while (rs.next()) {
				title = new Title();
				title.setName(rs.getString(1));
				title.setAbbr(rs.getBoolean(2));
				title.setPriority(rs.getInt(3));
				title.setLang(rs.getString(4));
				te.addTitle(title);
			}
			te.setTitles();
			rs.close();
		}
		stmt.close();
	}
	
	protected void addBible(Bible bible, Connection conn) throws Exception {
		if (conn == null) {
			conn = this.getSharedConnection();
		}
		String sql = "INSERT INTO Bible (Name, Lang, IsTemplate, IdTemplate, IdAuthor, IdCover, Version, Description, Comments) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, bible.getName());
		stmt.setString(2, bible.getLang());
		stmt.setBoolean(3, bible.getIsTemplate());
		if (bible.getIdTemplate() == null) {
			if (bible.getTemplate() != null) {
				String templateName = bible.getTemplate().getName();
				if (templateName != null) {
					Object id = executeScalar(conn, "SELECT Id FROM Bible WHERE Name='" + templateName + "'");
					if (id != null) {
						bible.setIdTemplate(Integer.parseInt(id.toString()));
					}
				}
			}
		}
		if (bible.getIdTemplate() == null) {
			stmt.setNull(4, Types.INTEGER);
		} else {
			stmt.setInt(4, bible.getIdTemplate());
		}
		if (bible.getIdAuthor() == null) {
			stmt.setNull(5, Types.INTEGER);
		} else {
			stmt.setInt(5, bible.getIdAuthor());
		}
		if (bible.getIdCover() == null) {
			stmt.setNull(6, Types.INTEGER);
		} else {
			stmt.setInt(6, bible.getIdCover());
		}
		stmt.setString(7, bible.getVersion());
		stmt.setString(8, bible.getDescription());
		stmt.setString(9, bible.getComments());
		stmt.executeUpdate();
	}
	
	protected void deleteBible(Bible bible, Connection conn) throws Exception {
		String bibleTable = bible.getName();
		String wordTable = getWordTable(bibleTable);
		executeUpdate(conn, "DROP TABLE " + bibleTable + " IF EXISTS");
		executeUpdate(conn, "DROP TABLE " + wordTable + " IF EXISTS");
		executeUpdate(conn, "DELETE FROM Bible WHERE Name = '" + bibleTable + "'");
	}
	
	protected <T extends TitleEntity> String getAliasTable(Class<T> c) {
		String name = c.getName();
		int i = name.lastIndexOf('.');
		if (i != -1) {
			name = name.substring(i + 1);
		}
		return name + "Alias";
	}
	
	protected String getWordTable(String bibleName) {
		return bibleName + "Words";
	}
	
	protected boolean tableExists(String tableName, Connection conn) throws Exception {
		if (conn == null) {
			conn = this.getSharedConnection();
		}
		java.sql.DatabaseMetaData meta = conn.getMetaData();
	    ResultSet tables = meta.getTables("PUBLIC", "PUBLIC", null, null);
	    while (tables.next()) {
	      String name = tables.getString(3);    // "TABLE_NAME"
	      if (name.equalsIgnoreCase(tableName)) {
	    	  return true;
	      }
	    }
		return false;
	}
	
	protected void checkFirstRun() throws Exception {
		if (!tableExists("Bible", null)) {
			java.io.InputStream input = HsqlDbEngine.class.getResourceAsStream(SCRIPT);
			if (input == null) {
				throw new Exception("SQL Script not found");
			}
			java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(input));
			String line = null;
			SqlTransaction trans = new SqlTransaction(this);
			trans.begin();
			try {
				Connection conn = trans.getConnection();
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (!StringUtils.EMPTY.equals(line) && !line.startsWith("--")) {
						conn.createStatement().execute(line);
					}
				}
				trans.commit();
				System.out.println("DB Schema created succesfully (First time entry)");
			} catch (Exception e) {
				trans.rollback();
				System.out.println(line);
				e.printStackTrace();
			}
		}
	}
	
	protected <T extends TitleEntity> Integer getIdFromAlias(Class<T> c, String alias, Connection conn) throws Exception {
		String table = getAliasTable(c);
		if (conn == null) {
			conn = this.getSharedConnection();
		}
		String sql = "SELECT Id FROM " + table + " WHERE Name = '" + alias + "'";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Integer result = null;
		if (rs.next()) {
			result = rs.getInt(1);
		}
		rs.close();
		return result;
	}
}
