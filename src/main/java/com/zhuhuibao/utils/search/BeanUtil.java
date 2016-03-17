package com.zhuhuibao.utils.search;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public class BeanUtil {

	@Target({ ElementType.METHOD })
	@Retention(value = RetentionPolicy.RUNTIME)
	public static @interface BeanIgnore {
	}

	@SuppressWarnings("serial")
	public static class BeanException extends Exception {

		public BeanException(String message) {
			super(message);
		}

		public BeanException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	private static String getFieldName(String methodName, int offset) {
		if (methodName.length() <= offset) {
			return null;
		}
		String field = methodName.substring(offset);
		if (field.length() == 1) {
			return field.toLowerCase();
		}
		return field.toLowerCase().charAt(0) + field.substring(1);
	}

	private static Object parseIfIsJSONString(Object raw) {
		if (raw instanceof String) {
			String s = (String) raw;
			if (JSONUtil.isJSONString(s)) {
				return JSON.parse(s);
			}
		}
		return raw;
	}

	public static Object parse(Object raw, Class<?> destClass)
			throws BeanException {
		return parse(raw, destClass, null);
	}

	public static Object parse(Object raw, Class<?> destClass,
			ParameterizedType pType) throws BeanException {
		if (raw == null) {
			return null;
		}
		if (raw.getClass().equals(destClass)) {
			return raw;
		}

		// most case
		if (destClass.equals(String.class)) {
			return raw.toString();
		}
		if (destClass.equals(Boolean.class)) {
			return FormatUtil.parseBooleanStrictly(raw);
		}
		if (destClass.equals(boolean.class)) {
			return FormatUtil.parseBoolean(raw, Boolean.FALSE);
		}
		if (destClass.equals(Integer.class)) {
			return FormatUtil.parseInteger(raw);
		}
		if (destClass.equals(int.class)) {
			Integer integer = FormatUtil.parseInteger(raw);
			return integer == null ? 0 : integer.intValue();
		}
		if (destClass.equals(Long.class)) {
			return FormatUtil.parseLong(raw);
		}
		if (destClass.equals(long.class)) {
			Long l = FormatUtil.parseLong(raw);
			return l == null ? 0L : l.longValue();
		}
		if (destClass.equals(Double.class)) {
			return FormatUtil.parseDouble(raw);
		}
		if (destClass.equals(double.class)) {
			Double d = FormatUtil.parseDouble(raw);
			return d == null ? 0d : d.doubleValue();
		}
		if (destClass.equals(Float.class)) {
			return FormatUtil.parseFloat(raw);
		}
		if (destClass.equals(float.class)) {
			Float f = FormatUtil.parseFloat(raw);
			return f == null ? 0f : f.floatValue();
		}
		if (destClass.equals(Date.class)) {
			if (raw instanceof Date) {
				return raw;
			}
			if (raw instanceof Number) {
				return FormatUtil.timestamp2date(FormatUtil.parseLongValue(raw, -1));
			} else {
				return FormatUtil.parseISO8601Date(raw.toString());
			}
		}
		if (destClass.equals(Short.class)) {
			return FormatUtil.parseShort(raw);
		}
		if (destClass.equals(short.class)) {
			Short s = FormatUtil.parseShort(raw);
			return s == null ? (short) 0 : s.shortValue();
		}
		if (destClass.equals(Byte.class)) {
			return FormatUtil.parseByte(raw);
		}
		if (destClass.equals(byte.class)) {
			Byte s = FormatUtil.parseByte(raw);
			return s == null ? (byte) 0 : s.byteValue();
		}
		if (destClass.equals(Character.class) || destClass.equals(char.class)) {
			return raw.toString().charAt(0);
		}

		// Array
		if (destClass.isArray()) {
			raw = parseIfIsJSONString(raw);
			return parseArray(raw, destClass.getComponentType());
		}

		// Map
		if (Map.class.isAssignableFrom(destClass)) {
			raw = parseIfIsJSONString(raw);
			Class<?> valueClass = Object.class;
			Type type = (Type) destClass;
			if (type instanceof ParameterizedType) {
				pType = (ParameterizedType) type;
			}
			if (pType != null) {
				Type valueType = pType.getActualTypeArguments()[1];
				if (valueType instanceof Class) {
					valueClass = (Class<?>) valueType;
				}
			}
			@SuppressWarnings("unchecked")
			Map<?, Object> rawMap = (Map<?, Object>) raw;
			for (Entry<?, Object> entry : rawMap.entrySet()) {
				entry.setValue(parse(entry.getValue(), valueClass, null));
			}
			return rawMap;
		}

		// List/Set
		if (Collection.class.isAssignableFrom(destClass)) {
			raw = parseIfIsJSONString(raw);
			Class<?> componentClass = Object.class;
			Type type = (Type) destClass;
			if (type instanceof ParameterizedType) {
				pType = (ParameterizedType) type;
			}
			if (pType != null) {
				Type componentType = pType.getActualTypeArguments()[0];
				if (componentType instanceof Class) {
					componentClass = (Class<?>) componentType;
				}
			}
			if (List.class.isAssignableFrom(destClass)) {
				return parseCollection(raw, componentClass,
						new ArrayList<Object>());
			} else if (Set.class.isAssignableFrom(destClass)) {
				return parseCollection(raw, componentClass,
						new HashSet<Object>());
			}
		}

		// bean
		if (destClass.equals(Object.class)) {
			return raw;
		}

		raw = parseIfIsJSONString(raw);
		if (Map.class.isAssignableFrom(raw.getClass())) {
			Object object;
			try {
				object = destClass.newInstance();
			} catch (Exception e) {
				throw new BeanException("Failed to create instance of "
						+ destClass, e);
			}
			return BeanUtil.map2bean((Map<?, ?>) raw, object);
		}

		return raw;
	}

	public static Object parseCollection(Object raw, Class<?> componentType,
			Collection<Object> newCollection) throws BeanException {
		if (raw.getClass().isArray()) {
			int length = Array.getLength(raw);
			for (int i = 0; i < length; i++) {
				Object item = Array.get(raw, i);
				Object parsedItem = parse(item, componentType);
				newCollection.add(parsedItem);
			}
			return newCollection;
		} else if (raw instanceof Collection) {
			Collection<?> collection = (Collection<?>) raw;
			for (Object item : collection) {
				Object parsedItem = parse(item, componentType);
				newCollection.add(parsedItem);
			}
			return newCollection;
		}
		throw new IllegalArgumentException("Failed to parseCollection: "
				+ raw.getClass());
	}

	public static Object parseArray(Object raw, Class<?> componentType)
			throws BeanException {
		if (raw.getClass().isArray()) {
			int length = Array.getLength(raw);
			Object newArray = Array.newInstance(componentType, length);
			for (int i = 0; i < length; i++) {
				Object item = Array.get(raw, i);
				Object parsedItem = parse(item, componentType);
				Array.set(newArray, i, parsedItem);
			}
			return newArray;
		} else if (raw instanceof Collection) {
			Collection<?> collection = (Collection<?>) raw;
			int length = collection.size();
			Object newArray = Array.newInstance(componentType, length);
			int i = 0;
			for (Object item : collection) {
				Object parsedItem = parse(item, componentType);
				Array.set(newArray, i, parsedItem);
				i++;
			}
			return newArray;
		}
		throw new IllegalArgumentException("Failed to parseArray: "
				+ raw.getClass());
	}

	public static <T> T map2bean(Map<?, ?> map, T bean) throws BeanException {
		Method[] methods = bean.getClass().getMethods();
		String methodName = null;
		String key = null;
		Object value = null;
		Map<String, Field> fields = getAllFields(bean.getClass());
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			Class<?>[] paramTypes = method.getParameterTypes();
			if (paramTypes.length != 1) {
				continue;
			}
			if (method.isAnnotationPresent(BeanIgnore.class)) {
				continue;
			}
			methodName = method.getName();
			if (!methodName.startsWith("set")) {
				continue;
			}
			key = getFieldName(methodName, 3);
			if (key == null) {
				continue;
			}
			value = map.get(key);
			if (value == null) {
				continue;
			}
			ParameterizedType pType = null;
			Field field = fields.get(key);
			if (field != null) {
				Type type = field.getGenericType();
				if (type instanceof ParameterizedType) {
					pType = (ParameterizedType) type;
				}
			}
			value = parse(value, method.getParameterTypes()[0], pType);
			try {
				method.invoke(bean, value);
			} catch (Exception e) {
				throw new BeanException("Failed to invoke " + method.getName()
						+ " of " + method.getDeclaringClass().getName(), e);
			}
		}
		return bean;
	}

	public static Map<String, Object> bean2serializedMap(Object bean)
			throws BeanException {
		Map<String, Object> map = JSONUtil.parseAsMap(JSONUtil
				.toJSONString(bean));
		if (map == null) {
			throw new BeanException("Failed to call bean2serializedMap");
		}
		return map;
	}

	public static <T> T extend(T dest, Object src) throws BeanException {
		BeanUtil.map2bean(BeanUtil.bean2map(src), dest);
		return dest;
	}

	private static void getAllFields(Class<?> clazz, Map<String, Field> fieldMap) {
		for (Field field : clazz.getDeclaredFields()) {
			if (!fieldMap.containsKey(field.getName())) {
				fieldMap.put(field.getName(), field);
			}
		}
		if (clazz.getSuperclass() != null) {
			getAllFields(clazz.getSuperclass(), fieldMap);
		}
	}

	public static Map<String, Field> getAllFields(Class<?> clazz) {
		Map<String, Field> fields = new HashMap<String, Field>();
		getAllFields(clazz, fields);
		return fields;
	}

	public static Map<String, Object> bean2map(Object bean)
			throws BeanException {
		Map<String, Object> map = new HashMap<String, Object>();
		Method[] methods = bean.getClass().getMethods();
		String methodName = null;
		String field = null;
		Object value = null;
		for (Method method : methods) {
			if (Modifier.isStatic(method.getModifiers())) {
				continue;
			}
			Class<?>[] paramTypes = method.getParameterTypes();
			if (paramTypes.length != 0) {
				continue;
			}
			if (method.isAnnotationPresent(BeanIgnore.class)) {
				continue;
			}
			methodName = method.getName();
			int offset = 0;
			if (methodName.startsWith("get")) {
				offset = 3;
			} else if (methodName.startsWith("is")) {
				offset = 2;
			} else {
				continue;
			}
			field = getFieldName(methodName, offset);
			if (field == null || field.equals("class")) {
				continue;
			}
			try {
				value = method.invoke(bean);
			} catch (Exception e) {
				throw new BeanException("Failed to invoke " + method.getName()
						+ " of " + method.getDeclaringClass().getName(), e);
			}
			map.put(field, value);
		}
		return map;
	}
	// public static List<String> getFields(Class<?> clazz) {
	// Method[] methods = clazz.getMethods();
	// String methodName = null;
	// String field = null;
	// List<String> fields = new ArrayList<String>();
	// for (Method method : methods) {
	// if (!Modifier.isPublic(method.getModifiers())) {
	// continue;
	// }
	// if (Modifier.isStatic(method.getModifiers())) {
	// continue;
	// }
	// Class<?>[] paramTypes = method.getParameterTypes();
	// if (paramTypes.length != 0) {
	// continue;
	// }
	// if (method.isAnnotationPresent(BeanIgnore.class)) {
	// continue;
	// }
	// methodName = method.getName();
	// int offset = 0;
	// if (methodName.startsWith("get")) {
	// offset = 3;
	// } else if (methodName.startsWith("is")) {
	// offset = 2;
	// } else {
	// continue;
	// }
	// field = getFieldName(methodName, offset);
	// if (field == null || field.equals("class")) {
	// continue;
	// }
	// fields.add(field);
	// }
	// return fields;
	// }
}
