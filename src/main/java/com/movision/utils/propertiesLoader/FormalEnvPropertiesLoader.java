package com.movision.utils.propertiesLoader;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/3 11:22
 */
public class FormalEnvPropertiesLoader {
    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("web/formal_env.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    /**
     * 测试一下
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(FormalEnvPropertiesLoader.getValue("post.incise.domain"));
    }
}
