package com.zhuhuibao.mybatis.expert.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class Dynamic {
    private String id;

    private String createId;

    private String publishTime;

    private String updateTime;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="关键字 多个关键字逗号隔开")
    private String wordkey;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty(value="缩略图地址")
    private String imgurl;

    private String views;

    private String status;

    private String is_deleted;

    @ApiModelProperty(value="内容")
    private String content;

    private String createName;

    @ApiModelProperty(value="发布人类型：1：运营人员；2：会员",required = true)
    private String createrType;

    @ApiModelProperty(value="拒绝理由")
    private String reason;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWordkey() {
        return wordkey;
    }

    public void setWordkey(String wordkey) {
        this.wordkey = wordkey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreaterType() {
        return createrType;
    }

    public void setCreaterType(String createrType) {
        this.createrType = createrType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}