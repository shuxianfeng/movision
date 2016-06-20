package com.zhuhuibao.mybatis.advertising.entity;

import java.util.Date;

public class SysAdvertising {
    private Integer id;

    private String title;

    private String advAttrId;

    private String advType;

    private String linkUrl;

    private String imgUrl;

    private Date createTime;

    private Integer operatorId;

    private String connectedId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getAdvAttrId() {
        return advAttrId;
    }

    public void setAdvAttrId(String advAttrId) {
        this.advAttrId = advAttrId == null ? null : advAttrId.trim();
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType == null ? null : advType.trim();
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getConnectedId() {
        return connectedId;
    }

    public void setConnectedId(String connectedId) {
        this.connectedId = connectedId == null ? null : connectedId.trim();
    }
}