package com.movision.utils.im;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 加载IM的配置参数
 *
 * @Author zhuangyuhao
 * @Date 2017/3/6 16:42
 */
public class ImPropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(ImPropertiesLoader.class);

    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils
                    .loadAllProperties("web/im.properties");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载im.properties属性文件出错：" + e.getMessage());
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        String app_key = ImPropertiesLoader.getPropertyValue("app_key");
        System.out.println(app_key);
    }
}
