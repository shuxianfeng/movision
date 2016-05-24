package com.zhuhuibao.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.zhuhuibao.fsearch.utils.FileUtil;
import com.zhuhuibao.fsearch.utils.FileUtil.ReadLineHandler;

public class PropertiesConfig extends Config {

	public PropertiesConfig(InputStream in, boolean unicode) {
		try {
			final Properties props = new Properties();
			if (unicode) {
				try {
					props.load(in);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				try {
					FileUtil.read(in, null, new ReadLineHandler() {
						private String concatKey = null;
						private String concatStr = null;

						public String[] getKV(String s) {
							int offset = s.indexOf('=');
							if (offset <= 0) {
								throw new RuntimeException("Bad config line: "
										+ s);
							}
							String key = s.substring(0, offset).trim();
							String value = s.substring(offset + 1).trim();
							return new String[] { key, value };
						}

						private String replaceSpecialChars(String s) {
							StringBuilder sb = new StringBuilder();
							int len = s.length();
							for (int i = 0; i < len; i++) {
								char c = s.charAt(i);
								if (c == '\\' && i < len - 1) {
									boolean replace = true;
									char next = s.charAt(i + 1);
									switch (next) {
									case 'n':
										sb.append('\n');
										break;
									case 't':
										sb.append('\t');
										break;
									case 'r':
										sb.append('\r');
										break;
									case 'f':
										sb.append('\f');
										break;
									default:
										replace = false;
									}
									if (replace) {
										i++;
										continue;
									}
								}
								sb.append(c);
							}
							return sb.toString();
						}

						@Override
						public boolean handle(String s, int line)
								throws Exception {
							s = s.trim();
							if (s.isEmpty()) {
								return true;
							}
							if (s.charAt(0) == '#' && concatStr == null) {
								return true;
							}
							s = replaceSpecialChars(s);
							if (s.charAt(s.length() - 1) == '\\') {
								s = s.substring(0, s.length() - 1);
								if (concatStr == null) {
									String[] kv = getKV(s);
									concatKey = kv[0];
									concatStr = kv[1];
								} else {
									concatStr += s;
								}
								return true;
							} else if (concatStr != null) {
								concatStr += s;
								props.put(concatKey, concatStr);
								concatStr = null;
								return true;
							}
							String[] kv = getKV(s);
							props.put(kv[0], kv[1]);
							return true;
						}

					});
				} catch (Exception e) {
					if (e instanceof RuntimeException) {
						throw (RuntimeException) e;
					} else {
						throw new RuntimeException(e);
					}
				}
			}
			for (Entry<Object, Object> entry : props.entrySet()) {
				put(entry.getKey().toString(), entry.getValue(), true);
			}
		} finally {
			FileUtil.close(in);
		}
	}

	public PropertiesConfig(InputStream in) {
		this(in, true);
	}

	public PropertiesConfig(Map<?, ?> map) {
		for (Entry<?, ?> entry : map.entrySet()) {
			put(entry.getKey().toString(), entry.getValue(), true);
		}
	}

	@Override
	protected Object loadValue(String key) {
		return null;
	}

}
