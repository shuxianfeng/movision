package com.zhuhuibao.fsearch.repository.db;

import com.zhuhuibao.fsearch.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MapHandler extends RowHandler<Map<String, Object>> {
	private static final int TYPE_CASESENSITIVE = 1;
	private static final int TYPE_LOWERCASE = 2;
	private static final int TYPE_UPPERCASE = 3;
	private static final int TYPE_CAMELCASE = 4;

	private int type;

	public MapHandler(int type) {
		super();
		this.type = type;
	}

	@Override
	public Map<String, Object> handleRow(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();
		int columnCount = meta.getColumnCount();
		Map<String, Object> dataMap = new HashMap<String, Object>(columnCount);
		for (int i = 1; i <= columnCount; i++) {
//			String key = meta.getColumnName(i);
			String key = meta.getColumnLabel(i);
			Object value = rs.getObject(i);
			switch (type) {
			case TYPE_CASESENSITIVE:
				break;
			case TYPE_LOWERCASE:
				key = key.toLowerCase();
				break;
			case TYPE_UPPERCASE:
				key = key.toUpperCase();
				break;
			case TYPE_CAMELCASE:
				key = StringUtil.underline2camel(key);
			}
			dataMap.put(key, value);
		}

		return dataMap;
	}

	public static final MapHandler CASESENSITIVE = new MapHandler(
			TYPE_CASESENSITIVE);
	public static final MapHandler LOWERCASE = new MapHandler(TYPE_LOWERCASE);
	public static final MapHandler UPPERCASE = new MapHandler(TYPE_UPPERCASE);
	public static final MapHandler CAMELCASE = new MapHandler(TYPE_CAMELCASE);
}