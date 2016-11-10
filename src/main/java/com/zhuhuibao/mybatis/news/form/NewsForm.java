package com.zhuhuibao.mybatis.news.form;

import com.zhuhuibao.mybatis.news.entity.News;

/**
 * 资讯实体返回类
 *
 * @author liyang
 * @date 2016年10月18日
 */
public class NewsForm {

    /**
     * 资讯实体类
     */
    private News news;

    /**
     * 状态
     */
    private String statusText;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 显示一级分类和二级分类的组合名称
     */
    private String showTypeName;

    /**
     * 一级分类名称
     */
    private String typeName;

    /**
     * 二级分类名称
     */
    private String subTypeName;

    /**
     * 发布时间
     */
    private String publishTimeStr;

    /**
     * 推荐位置
     */
    private String recPlace;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getShowTypeName() {
        return showTypeName;
    }

    public void setShowTypeName(String showTypeName) {
        this.showTypeName = showTypeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getPublishTimeStr() {
        return publishTimeStr;
    }

    public void setPublishTimeStr(String publishTimeStr) {
        this.publishTimeStr = publishTimeStr;
    }

    public String getRecPlace() {
        return recPlace;
    }

    public void setRecPlace(String recPlace) {
        this.recPlace = recPlace;
    }
}
