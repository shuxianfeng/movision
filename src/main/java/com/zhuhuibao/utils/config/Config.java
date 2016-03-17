package com.zhuhuibao.utils.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zhuhuibao.utils.search.CollectionUtil;
import com.zhuhuibao.utils.search.FormatUtil;
import com.zhuhuibao.utils.search.JSONUtil;
import com.zhuhuibao.utils.search.StringUtil;

public abstract class Config {

	public static interface ConfigValueChangedListener {
		void onChanged(String key, Object value);
	}

	private volatile Map<String, Object> dataSource = new HashMap<String, Object>(
			0);
	private volatile List<ConfigValueChangedListener> listeners = new ArrayList<ConfigValueChangedListener>(
			0);

	public void put(String key, Object value, boolean fireChangedEvent) {
		dataSource.put(key, value);
		if (fireChangedEvent && CollectionUtil.isNotEmpty(listeners)) {
			for (ConfigValueChangedListener listener : listeners) {
				listener.onChanged(key, value);
			}
		}
	}

	public void setDataSource(Map<String, Object> ds, boolean fireChangedEvent) {
		if (ds == null) {
			throw new IllegalArgumentException("DataSource should not be null");
		}
		Map<String, Object> oldDataSource = this.dataSource;
		this.dataSource = ds;
		if (fireChangedEvent && CollectionUtil.isNotEmpty(listeners)
				&& oldDataSource != null) {
			for (Entry<String, Object> entry : oldDataSource.entrySet()) {
				String key = entry.getKey();
				Object newValue = ds.get(key);
				for (ConfigValueChangedListener listener : listeners) {
					listener.onChanged(key, newValue);
				}
			}
		}
	}

	public Map<String, Object> getAll() {
		return dataSource;
	}

	public boolean containsKey(String key) {
		return dataSource.containsKey(key);
	}

	public void addListener(ConfigValueChangedListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ConfigValueChangedListener listener) {
		listeners.remove(listener);
	}

	public Object getValue(String key) {
		if (StringUtil.isEmpty(key)) {
			throw new IllegalArgumentException("Empty key");
		}
		if (dataSource.containsKey(key)) {
			return dataSource.get(key);
		}
		Object value = loadValue(key);
		dataSource.put(key, value);
		return value;
	}

	public Object ensureValue(String key) {
		Object value = getValue(key);
		if (value == null) {
			throw new IllegalArgumentException("No value set for key '" + key
					+ "'");
		}
		return value;
	}

	public String getString(String key) {
		return FormatUtil.parseString(ensureValue(key));
	}

	public String getString(String key, String defaultValue) {
		return FormatUtil.parseString(getValue(key), defaultValue);
	}

	public int getInt(String key) {
		return FormatUtil.parseInteger(ensureValue(key)).intValue();
	}

	public int getInt(String key, int defaultValue) {
		return FormatUtil.parseIntValue(getValue(key), defaultValue);
	}

	public long getLong(String key) {
		return FormatUtil.parseLong(ensureValue(key)).longValue();
	}

	public long getLong(String key, long defaultValue) {
		return FormatUtil.parseLongValue(getValue(key), defaultValue);
	}

	public double getDouble(String key) {
		return FormatUtil.parseDouble(ensureValue(key)).doubleValue();
	}

	public double getDouble(String key, double defaultValue) {
		return FormatUtil.parseDoubleValue(getValue(key), defaultValue);
	}

	public boolean getBoolean(String key) {
		String v = getString(key);
		return v.equals("1") || v.equals("true");
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return FormatUtil.parseBoolean(getValue(key),
				Boolean.valueOf(defaultValue)).booleanValue();
	}

	public Map<?, ?> getJSONObject(String key) {
		Object value = ensureValue(key);
		if (value instanceof Map) {
			return (Map<?, ?>) value;
		}
		return JSONUtil.parseAsMap(value.toString());
	}

	public Map<?, ?> getJSONObject(String key, Map<?, ?> defaultValue) {
		Object value = getValue(key);
		if (value == null) {
			return defaultValue;
		}
		if (value instanceof Map) {
			return (Map<?, ?>) value;
		}
		return JSONUtil.parseAsMap(value.toString());
	}

	public byte[] getBlob(String key) {
		return (byte[]) ensureValue(key);
	}

	public byte[] getBlob(String key, byte[] defaultValue) {
		byte[] blob = (byte[]) getValue(key);
		return blob == null ? defaultValue : blob;
	}

	protected abstract Object loadValue(String key);
}
