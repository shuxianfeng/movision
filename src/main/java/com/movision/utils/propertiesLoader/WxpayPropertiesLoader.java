package com.movision.utils.propertiesLoader;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

/**
 * 微信支付配置文件加载器
 *
 * @author zhuangyuhao
 * @time 2016年10月14日 下午4:41:48
 */
public class WxpayPropertiesLoader {
    private static final Logger logger = LoggerFactory.getLogger(WxpayPropertiesLoader.class);

    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils
                    .loadAllProperties("web/wxpay.properties");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载wxpay.properties属性文件出错：" + e.getMessage());
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        String HTTPS_VERIFY_URL = WxpayPropertiesLoader.getPropertyValue("get_openid_url");
        System.out.println(HTTPS_VERIFY_URL);
    }
}
