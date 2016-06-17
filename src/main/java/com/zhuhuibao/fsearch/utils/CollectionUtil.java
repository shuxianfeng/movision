package com.zhuhuibao.fsearch.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionUtil {

	public static Map<String, Object> arrayAsMap(Object... t) {
		if (t == null || t.length <= 0) {
			return null;
		}
		if (t.length % 2 != 0) {
			throw new RuntimeException("illegal args count");
		}
		Map<String, Object> params = new HashMap<String, Object>(t.length);
		for (int i = 0; i < t.length; i += 2) {
			if (t[i] == null || !t[i].getClass().equals(String.class)) {
				throw new RuntimeException("illegal arg: " + t[i] + "at " + i);
			}
			String key = t[i].toString();
			Object value = t[i + 1];
			params.put(key, value);
		}
		return params;
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return collection != null && !collection.isEmpty();
	}

	public static <T extends Collection<?>> T trimToNull(T collection) {
		return isEmpty(collection) ? null : collection;
	}

	public static <T extends Object> T trimArrayToNull(T array) {
		if (array == null) {
			return null;
		}
		if (Array.getLength(array) == 0) {
			return null;
		}
		return array;
	}

	public static <T> List<T> addToList(List<T> dest, T src) {
		if (src != null && !dest.contains(src)) {
			dest.add(src);
		}
		return dest;
	}

	public static <T> List<T> addAllToList(List<T> dest, Collection<T> src) {
		if (isEmpty(src)) {
			return dest;
		}
		for (T item : src) {
			if (!dest.contains(item)) {
				dest.add(item);
			}
		}
		return dest;
	}

}
