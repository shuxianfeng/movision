package com.movision.utils.propertiesLoader;

import com.movision.common.constant.MsgCodeConstant;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;


public class MsgPropertiesLoader {

    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("web/message.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(MsgPropertiesLoader.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
    }

}
