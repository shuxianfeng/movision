package com.movision.common.constant;

import com.movision.utils.im.ImPropertiesLoader;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/6 16:47
 */
public class ImConstant {

    public static final String APP_KEY = ImPropertiesLoader.getPropertyValue("app_key");

    public static final String APP_SECRET = ImPropertiesLoader.getPropertyValue("app_secret");


    //-------------------------------------**********-----------------------------------------------

    public static final String CREATE_USER_URL = ImPropertiesLoader.getPropertyValue("create_user_url");

    public static final String UPDATE_USER_URL = ImPropertiesLoader.getPropertyValue("update_user_url");


}
