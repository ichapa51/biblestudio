package org.biblestudio.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class ObjectCache <K> {

	Map<Class<?>,Map<K,Object>> cache;
	
	public ObjectCache() {
		cache = createClassMap();
	}
	
	protected Map<Class<?>,Map<K,Object>> createClassMap() {
		return new Hashtable<Class<?>,Map<K,Object>>();
	}
	
	protected Map<K,Object> createKeyMap() {
		return new HashMap<K,Object>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getTyped(Class<T> c, K key) {
		if (cache.containsKey(c)) {
			return (T)cache.get(c).get(key);
		}
		return null;
	}
	
	public Object get(Class<?> c, K key) {
		if (cache.containsKey(c)) {
			return cache.get(c).get(key);
		}
		return null;
	}
	
	public boolean containsKey(Class<?> c, K key) {
		if (cache.containsKey(c)) {
			return cache.get(c).containsKey(key);
		}
		return false;
	}
	
	public void put(Class<?> c, K key, Object value) {
		if (!cache.containsKey(c)) {
			cache.put(c, createKeyMap());
		}
		cache.get(c).put(key, value);
	}
	
	public void clear() {
		cache = createClassMap();
	}
}
