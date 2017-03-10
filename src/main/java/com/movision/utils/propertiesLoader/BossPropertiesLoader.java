package com.movision.utils.propertiesLoader;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/10 10:33
 */
public class BossPropertiesLoader {
    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("web/boss.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(LoginPropertiesLoader.getValue("boss_superadmin_role"));
    }
}
