package com.movision.utils;


import com.movision.utils.config.Config;
import com.movision.utils.config.PropertiesConfig;

/**
 * 读取配置文件
 *
 * @Author zhuangyuhao
 * @Date 2017/3/20 14:51
 */
public class G {
    private static final PropertiesConfig config = new PropertiesConfig(
            G.class.getResourceAsStream("/config/config.ini"), false);

    public static Config getConfig() {
        return config;
    }
}
