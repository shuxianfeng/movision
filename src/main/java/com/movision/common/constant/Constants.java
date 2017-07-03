package com.movision.common.constant;

import com.movision.utils.propertiesLoader.PropertiesLoader;

public class Constants {
    /**
     * 短信验证时间 10分钟
     */
    public static final String sms_time = "10";


    /**
     * 手机验证码的长度 6
     */
    public static final Integer CHECK_MOBILE_CODE_SIZE = 6;


    /**
     * 是默认地址
     */
    public static final Integer DEFAULT_ADDRESS = 1;
    /**
     * 不是默认地址
     */
    public static final Integer NOT_DEFAULT_ADDRESS = 0;

    /**
     * 测试服务器的ip
     */
    public static final String TEST_SERVER_IP = PropertiesLoader.getValue("test.server.ip");
    /**
     * 正式服务器的ip
     */
    public static final String FORMAL_SERVER_IP = PropertiesLoader.getValue("formal.server.ip");
}
