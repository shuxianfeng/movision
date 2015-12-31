package com.zhuhuibao.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 接口平台调用参数
 * Created by jianglz on 2014/5/17.
 */
@Component
public class ApiConstants {
    @Value("${apiUrl}")
    private String apiUrl;
    @Value("${apiToken}")
    private String apiToken;
    @Value("${appkey}")
    private String appKey;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
}
