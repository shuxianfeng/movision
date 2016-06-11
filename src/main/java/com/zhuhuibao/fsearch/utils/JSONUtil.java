package com.zhuhuibao.fsearch.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSONUtil {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JSONUtil.class);
	private static Map<Type, ObjectSerializer> ADDITIONAL_SERIALIZERS = new HashMap<Type, ObjectSerializer>();
	private static SerializeConfig SERIALIZE_CONFIG = new SerializeConfig();
	private static final ISO8601DateWithMillsSerializer ISO8601DATEWITHMILLS_SERIALIZER = new ISO8601DateWithMillsSerializer();

	static {
		ADDITIONAL_SERIALIZERS.put(java.util.Date.class,
				ISO8601DATEWITHMILLS_SERIALIZER);
		ADDITIONAL_SERIALIZERS.put(java.sql.Timestamp.class,
				ISO8601DATEWITHMILLS_SERIALIZER);
		ADDITIONAL_SERIALIZERS.put(java.sql.Date.class,
				ISO8601DATEWITHMILLS_SERIALIZER);
		for (Entry<Type, ObjectSerializer> entry : ADDITIONAL_SERIALIZERS
				.entrySet()) {
			SERIALIZE_CONFIG.put(entry.getKey(), entry.getValue());
		}
	}

	public static class ISO8601DateWithMillsSerializer implements
			ObjectSerializer {
		
		public void write(JSONSerializer serializer, Object object,
				Object fieldName, Type fieldType) throws IOException {
			if (object == null) {
				serializer.getWriter().writeNull();
			} else {
				serializer.write(FormatUtil.formatISO8601Date((Date) object));
			}
		}
	}

	public static String toJSONString(Object object) {
		if (object == null) {
			return null;
		}
		return JSON.toJSONString(object, SERIALIZE_CONFIG,
				SerializerFeature.DisableCircularReferenceDetect);
	}

	public static String toJSONString(Object object,
			Map<Type, ObjectSerializer> serializers) {
		if (object == null) {
			return null;
		}
		SerializeConfig sc = new SerializeConfig();
		for (Entry<Type, ObjectSerializer> entry : ADDITIONAL_SERIALIZERS
				.entrySet()) {
			sc.put(entry.getKey(), entry.getValue());
		}
		for (Entry<Type, ObjectSerializer> entry : serializers.entrySet()) {
			sc.put(entry.getKey(), entry.getValue());
		}
		return JSON.toJSONString(object, sc,
				SerializerFeature.DisableCircularReferenceDetect);
	}

	public static Object parse(String s) {
		if (StringUtil.isEmpty(s)) {
			return null;
		}
		try {
			return JSON.parse(s);
		} catch (Throwable t) {
			LOGGER.error("Failed to parseJSON: " + s, t);
			return null;
		}
	}

	public static Map<String, Object> parseAsMap(String s) {
		try {
			Object o = parse(s);
			return (JSONObject) o;
		} catch (Throwable t) {
			LOGGER.error("Failed to parse to JSONObject", t);
			return null;
		}
	}

	public static List<?> parseAsList(String s) {
		try {
			Object o = parse(s);
			return (JSONArray) o;
		} catch (Throwable t) {
			LOGGER.error("Failed to parse to JSONArray", t);
			return null;
		}
	}

	public static <T> T parse(String s, Class<T> clazz) {
		Map<String, Object> map = parseAsMap(s);
		if (map == null) {
			return null;
		}
		try {
			return BeanUtil.map2bean(map, clazz.newInstance());
		} catch (Exception e) {
			LOGGER.error("Failed to parseBean: " + s, e);
			return null;
		}
	}

	public static boolean isJSONString(String s) {
		if (s == null) {
			return false;
		}
		char endChar = '0';
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (c == '{') {
				endChar = '}';
				break;
			}
			if (c == '[') {
				endChar = ']';
				break;
			}
			return false;
		}
		if (endChar == '0') {
			return false;
		}
		for (int i = s.length() - 1; i >= 0; i--) {
			char c = s.charAt(i);
			if (Character.isWhitespace(c)) {
				continue;
			}
			return c == endChar;
		}
		return false;
	}
}
