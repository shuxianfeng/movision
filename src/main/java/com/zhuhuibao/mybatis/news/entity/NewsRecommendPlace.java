package com.zhuhuibao.mybatis.news.entity;

import java.util.Date;

public class NewsRecommendPlace {
    private Long id;

    private Long newsId;

    private String recommendPlace;

    private Date addTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getRecommendPlace() {
        return recommendPlace;
    }

    public void setRecommendPlace(String recommendPlace) {
        this.recommendPlace = recommendPlace;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}