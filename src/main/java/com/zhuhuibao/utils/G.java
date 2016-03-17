package com.zhuhuibao.utils;

import com.zhuhuibao.utils.config.Config;
import com.zhuhuibao.utils.config.PropertiesConfig;


public class G {
	private static final PropertiesConfig config = new PropertiesConfig(
			G.class.getResourceAsStream("/config/config.ini"), false);

	public static Config getConfig() {
		return config;
	}
}
