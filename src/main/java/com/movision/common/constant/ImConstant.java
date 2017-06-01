package com.movision.common.constant;

import com.movision.utils.im.ImPropertiesLoader;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/6 16:47
 */
public class ImConstant {

    public static final String APP_KEY = ImPropertiesLoader.getPropertyValue("app_key");

    public static final String APP_SECRET = ImPropertiesLoader.getPropertyValue("app_secret");


    public static final Integer TYPE_APP = 1;
    public static final Integer TYPE_BOSS = 0;


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


    /**
     * 加好友
     */
    public static final String ADD_FRIEND = ImPropertiesLoader.getPropertyValue("add_friend");


    /**
     * 个人与个人 发消息
     */
    public static final String SEND_MSG = ImPropertiesLoader.getPropertyValue("send_msg");


    /**
     * 批量发送系统通知
     */
    public static final String SEND_BATCH_ATTACH_MSG = ImPropertiesLoader.getPropertyValue("send_batch_attach_msg");


    public static final String SEND_MSG_BATCH = ImPropertiesLoader.getPropertyValue("send_msg_attach");

}
