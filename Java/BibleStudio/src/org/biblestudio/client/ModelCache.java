package org.biblestudio.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biblestudio.model.ModelEntity;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 09/08/2011
 */
public class ModelCache {

	private Map<String,Object> cache;
	
	public ModelCache() {
		cache = new HashMap<String,Object>();
	}
	
	public void put(IdQuery query) {
		cache.put(query.paramString(), query.getResult());
	}
	
	public void put(ModelQuery query) {
		cache.put(query.paramString(), query.getResult());
	}
	
	public boolean contains(IdQuery query) {
		return cache.containsKey(query.paramString());
	}
	
	public boolean contains(ModelQuery query) {
		return cache.containsKey(query.paramString());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ModelEntity> T get(IdQuery query) {
		if (cache.containsKey(query.paramString())) {
			return (T)cache.get(query.paramString());
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends ModelEntity> List<T> get(ModelQuery query) {
		if (cache.containsKey(query.paramString())) {
			return (List<T>)cache.get(query.paramString());
		}
		return null;
	}
	
	public void clear() {
		cache.clear();
	}
}
