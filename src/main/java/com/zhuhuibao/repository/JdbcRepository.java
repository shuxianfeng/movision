package com.zhuhuibao.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.zhuhuibao.repository.db.JdbcTemplate;
import com.zhuhuibao.utils.search.FormatUtil;

public class JdbcRepository {

	public static final String TRANSACTION = "jdbcTM";

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	protected Map<String, Object> serializeDoc(Map<String, Object> doc) {
		Map<String, Object> parsedDoc = new HashMap<String, Object>(doc);
		for (Entry<String, Object> entry : parsedDoc.entrySet()) {
			Object value = entry.getValue();
			if (value == null) {
				continue;
			}
			if (value instanceof Date) {
				entry.setValue(FormatUtil.date2timestamp((Date) value));
			}
		}
		return parsedDoc;
	}
}
