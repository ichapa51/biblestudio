package org.biblestudio.client.db;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.biblestudio.client.ConnectionRequest;
import org.biblestudio.client.IdQuery;
import org.biblestudio.client.ModelQuery;
import org.biblestudio.client.QueryCondition;
import org.biblestudio.client.SearchExp;
import org.biblestudio.client.SearchQuery;
import org.biblestudio.client.SearchType;
import org.biblestudio.client.TagObject;
import org.biblestudio.model.Bible;
import org.biblestudio.model.ModelEntity;
import org.biblestudio.util.LocaleUtils;
import org.biblestudio.util.StringUtils;

public abstract class AbstractDbEngine implements BibleDbEngine {

	protected static Map<String,String> ignoredEsWords = createIgnoredEsWords();
	private ConnectionRequest connectionConfig;
	
	public AbstractDbEngine() {
		
	}
	
	protected static Map<String,String> createIgnoredEsWords() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("de", "de");
		map.put("que", "que");
		map.put("la", "la");
		map.put("el", "el");
		map.put("los", "los");
		map.put("en", "en");
		map.put("su", "su");
		map.put("por", "por");
		map.put("se", "se");
		map.put("del", "del");
		map.put("para", "para");
		map.put("las", "las");
		map.put("con", "con");
		map.put("sus", "sus");
		map.put("al", "al");
		map.put("lo", "lo");
		map.put("tu", "tu");
		map.put("mi", "mi");
		map.put("le", "le");
		return map;
	}
	
	public abstract void start() throws Exception;
	
	public abstract void shutdown() throws Exception;
	
	public abstract ConnectionRequest getDefaultConnectionConfig();
	
	protected abstract List<ModelEntity> getEntityList(Class<? extends ModelEntity> entityType, Map<String,QueryCondition> cond) throws Exception;
	
	protected abstract void searchPassage(SearchQuery query, SearchExp.Passage passage) throws Exception;
	
	protected abstract void searchKeyword(SearchQuery query, SearchExp.Keyword keyword) throws Exception;
	
	protected abstract void searchAuto(SearchQuery query, SearchExp.Passage passage, SearchExp.Keyword keyword) throws Exception;
	
	@Override
	public void start(ConnectionRequest config) throws Exception {
		if (config == null) {
			config = getDefaultConnectionConfig();
		}
		this.setConnectionConfig(config);
		start();
	}
	
	@Override
	public boolean isRunning() {
		return this.getConnectionConfig() != null;
	}
	
	@Override
	public void shutdown(TagObject obj) throws Exception {
		if (!isRunning()) {
			return;
		}
		shutdown();
		this.setConnectionConfig(null);
	}
	
	@Override
	public void idQuery(IdQuery query) throws Exception {
		ModelEntity result = getEntity(query.getEntityType(), query.getId());
		query.setResult(result);
	}
	
	@Override
	public void modelQuery(ModelQuery query) throws Exception {
		List<ModelEntity> result = getEntityList(query.getEntityType(), query.getCondition());
		query.setResult(result);
	}
	
	@Override
	public void searchQuery(SearchQuery query) throws Exception {
		String queryText = query.getText();
		SearchExp exp = new SearchExp(query.getSearchType(), queryText);
		try {
			exp.compile();
		} catch (Exception e) {
			return;
		}
		if (SearchType.Passage.equals(exp.getType())) {
			searchPassage(query, exp.getPassage());
		} else if (SearchType.Keyword.equals(exp.getType())) {
			searchKeyword(query, exp.getKeyword());
		} else {
			searchAuto(query, exp.getPassage(), exp.getKeyword());
		}
	}
	
	@Override
	public ConnectionRequest getConnectionConfig() {
		return connectionConfig;
	}
	
	protected Bible getDefaultBible() {
		return null; //TODO
	}
	
	protected ModelEntity getEntity(Class<? extends ModelEntity> entityType, int id) throws Exception {
		QueryCondition cond = new QueryCondition("id", "=", id + "");
		ModelQuery query = new ModelQuery(entityType, cond);
		modelQuery(query);
		return query.getFirstResult();
	}
	
	public void setConnectionConfig(ConnectionRequest connectionConfig) {
		this.connectionConfig = connectionConfig;
	}
	
	public Locale getUILocale() {
		if (getConnectionConfig() == null) {
			return Locale.getDefault();
		}
		return getConnectionConfig().getUILocale();
	}
	
	public Locale getDocLocale() {
		if (getConnectionConfig() == null) {
			return Locale.getDefault();
		}
		return getConnectionConfig().getDocLocale();
	}
	
	public boolean isIgnoredWord(String lang, String word) {
		if (word.length() < 2) {
			return true;
		}
		if (LocaleUtils.ES.equals(lang)) {
			return ignoredEsWords.containsKey(word);
		}
		return false;
	}
	
	public Map<String,Integer> getIndexWords(String lang, String paragraph) {
		String[] wordsArr = StringUtils.getWords(paragraph.toLowerCase());
		Map<String,Integer> map = new HashMap<String,Integer>(wordsArr.length);
		for (String word: wordsArr) {
			if (!isIgnoredWord(lang, word)) {
				if (!map.containsKey(word)) {
					map.put(word, 0);
				}
				map.put(word, map.get(word) + 1);
			}
		}
		return map;
	}
}
