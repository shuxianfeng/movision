package com.zhuhuibao.alipay.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;

/**
 * 支付宝配置读取
 */
public class AlipayPropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(AlipayPropertiesLoader.class);

    private static Properties properties = null;
    static {
        try {
            properties = PropertiesLoaderUtils
                    .loadAllProperties("web/alipay.properties");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载alipay.properties属性文件出错：" + e.getMessage());
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        String HTTPS_VERIFY_URL = AlipayPropertiesLoader.getPropertyValue("https_verify_url");
        System.out.println(HTTPS_VERIFY_URL);
    }
}
