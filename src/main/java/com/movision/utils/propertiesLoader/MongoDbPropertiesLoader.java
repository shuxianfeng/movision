package com.movision.utils.propertiesLoader;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author zhanglei
 * @Date 2017/7/24 16:12
 */
public class MongoDbPropertiesLoader {
    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("mongodb/mongodb.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(MongoDbPropertiesLoader.getValue("mongo.hostport"));
    }
}
