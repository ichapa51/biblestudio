package org.biblestudio.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
/**
 * 
 * @author Israel Chapa
 * Creation Date: 04/08/2011
 */
public final class ReflectionUtils {

	private ReflectionUtils() {
		
	}
	
	public static String getShortClassName(Class<?> clazz) {
		String name = clazz.getName();
		int i = name.lastIndexOf('.');
		if (i > 0) {
			return name.substring(i + 1);
		}
		return name;
	}
	
	public static boolean isScalar(Class<?> clazz) {
		if (clazz.isPrimitive() || clazz.getName().startsWith("java.lang.")) {
			return !clazz.getName().endsWith(".Class"); // Not exactly but it does the work
		}
		return false;
	}
	
	public static EntityField[] getAllSetFields(Class<?> clazz, Boolean scalars) {
		ArrayList<EntityField> list = new ArrayList<EntityField>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
				Class<?> type = method.getParameterTypes()[0];
				EntityField field = new EntityField(type, method.getName().substring(3));
				if (scalars == null) {
					list.add(field);
				} else if (scalars && isScalar(type)) {
					list.add(field);
				} else if (!scalars && !isScalar(type)) {
					list.add(field);
				}
			}
		}
		EntityField[] fields = new EntityField[list.size()];
		return list.toArray(fields);
	}
	
	public static EntityField[] getDeclaredSetFields(Class<?> clazz, Boolean scalars) {
		ArrayList<EntityField> list = new ArrayList<EntityField>();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("set") && method.getParameterTypes().length == 1) {
				Class<?> type = method.getParameterTypes()[0];
				EntityField field = new EntityField(type, method.getName().substring(3));
				if (scalars == null) {
					list.add(field);
				} else if (scalars && isScalar(type)) {
					list.add(field);
				} else if (!scalars && !isScalar(type)) {
					list.add(field);
				}
			}
		}
		EntityField[] fields = new EntityField[list.size()];
		return list.toArray(fields);
	}
	
	public static void setBeanValue(Class<?> clazz, Class<?> fieldType, String fieldName, Object instance, Object value) throws Exception {
		Method method = clazz.getMethod("set" + fieldName, fieldType);
		method.invoke(instance, value);
	}
	
	public static String toPseudoJsonString(Object obj) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		Method[] methods = obj.getClass().getMethods();
		int count = 0;
		for (Method method : methods) {
			if (method.getName().startsWith("get") && isScalar(method.getReturnType()) && method.getParameterTypes().length == 0) {
				if (count != 0) {
					buffer.append(",");
				}
				buffer.append("\"").append(method.getName().substring(3)).append("\"=");
				buffer.append(method.invoke(obj, new Object[]{}));
				count ++;
			}
		}
		buffer.append("}");
		return buffer.toString();
	}
}
