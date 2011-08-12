package org.biblestudio.util;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public class RelationCache<K> extends ObjectCache<K> {

	private ObjectCache<Class<?>> childCache;
	
	public RelationCache() {
		childCache = new ObjectCache<Class<?>>();
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getChildren(Class<?> parentClass, Class<T> childClass, K key) {
		if (childCache.containsKey(parentClass, childClass)) {
			Map<K, Object> map = (Map<K, Object>) childCache.get(parentClass, childClass);
			return (List<T>) map.get(key);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean containsChildren(Class<?> parentClass, Class<?> childClass, K key) {
		if (childCache.containsKey(parentClass, childClass)) {
			Map<K, Object> map = (Map<K, Object>) childCache.get(parentClass, childClass);
			return map.containsKey(key);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T> void put(Class<?> parentClass, Class<T> childClass, K key, List<T> children) {
		if (!childCache.containsKey(parentClass, childClass)) {
			childCache.put(parentClass, childClass, createKeyMap());
		}
		Map<K, Object> map = (Map<K, Object>) childCache.get(parentClass, childClass);
		map.put(key, children);
	}
	
	@Override
	public void clear() {
		super.clear();
		childCache = new ObjectCache<Class<?>>();
	}
}
