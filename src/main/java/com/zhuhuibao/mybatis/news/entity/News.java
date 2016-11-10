package com.zhuhuibao.mybatis.news.entity;

import java.util.Date;

import com.wordnik.swagger.annotations.ApiParam;

public class News {
    private Long id;

    @ApiParam(value = "资讯一级分类", required = true)
    private Integer type;

    @ApiParam(value = "资讯二级分类", required = true)
    private Integer subType;

    @ApiParam(value = "缩略图")
    private String photo;

    @ApiParam(value = "资讯标题")
    private String title;

    @ApiParam(value = "简短标题")
    private String shortTitle;

    @ApiParam(value = "跳转路径")
    private String jumpUrl;

    @ApiParam(value = "来源")
    private String source;

    @ApiParam(value = "关键字")
    private String keywords;

    @ApiParam(value = "文章描述：导读")
    private String introduction;

    @ApiParam(value = "热度/点击率")
    private Long views;

    @ApiParam(value = "发布时间")
    private Date publishTime;

    private Long publisherId;

    @ApiParam(value = "状态")
    private Integer status;

    @ApiParam(value = "附件名称")
    private String attachName;

    @ApiParam(value = "附件路径")
    private String attachUrl;

    private Date addTime;

    private Date updateTime;

    @ApiParam(value = "资讯内容")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachurl) {
        this.attachUrl = attachurl;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachName() {
        return attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}