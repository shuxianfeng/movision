package com.movision.utils.propertiesLoader;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * 登录配置文件加载工具
 *
 * @Author zhuangyuhao
 * @Date 2017/2/20 20:40
 */
public class LoginPropertiesLoader {

    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("web/login.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(LoginPropertiesLoader.getValue("no.intercept.url"));
    }
}
