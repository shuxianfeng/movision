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

    /**
     * 创建云信用户
     */
    public static final String CREATE_USER_URL = ImPropertiesLoader.getPropertyValue("create_user_url");

    /**
     * 更新用户的云信token
     */
    public static final String UPDATE_USER_TOKEN_URL = ImPropertiesLoader.getPropertyValue("update_user_accid_url");

    /**
     * 刷新用户云信token
     */
    public static final String REFRESH_USER_TOKEN_URL = ImPropertiesLoader.getPropertyValue("refresh_user_token_url");

    /**
     * 更新用户信息
     */
    public static final String UPDATE_USER_INFO_URL = ImPropertiesLoader.getPropertyValue("update_user_info_url");

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = ImPropertiesLoader.getPropertyValue("get_user_info");



}
