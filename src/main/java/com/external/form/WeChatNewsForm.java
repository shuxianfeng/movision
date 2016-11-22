package com.external.form;

import com.zhuhuibao.mybatis.weChat.entity.WeChatNews;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class WeChatNewsForm {

    /**
     * 微信资讯信息
     */
    private WeChatNews weChatNews;

    /**
     * 新增时间
     */
    private String addTimeStr;

    /**
     * 更新时间
     */
    private String updateTimeStr;

    /**
     * 类别名字
     */
    private String showTypeName;

    public WeChatNews getWeChatNews() {
        return weChatNews;
    }

    public void setWeChatNews(WeChatNews weChatNews) {
        this.weChatNews = weChatNews;
    }

    public String getAddTimeStr() {
        return addTimeStr;
    }

    public void setAddTimeStr(String addTimeStr) {
        this.addTimeStr = addTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getShowTypeName() {
        return showTypeName;
    }

    public void setShowTypeName(String showTypeName) {
        this.showTypeName = showTypeName;
    }
}
