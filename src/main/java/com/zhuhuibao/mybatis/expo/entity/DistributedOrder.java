package com.zhuhuibao.mybatis.expo.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class DistributedOrder {
    private String id;

    private String createid;

    private String publishTime;

    private String updateTime;

    private String updateManId;

    @ApiModelProperty(value="联系人名称")
    private String linkName;

    @ApiModelProperty(value="手机")
    private String mobile;

    private String status;

    @ApiModelProperty(value="定制类型：1：活动策划；2：展前服务；3：展期服务；4：展后服务")
    private String type;

    private String companyName;

    private String source;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
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

    public String getUpdateManId() {
        return updateManId;
    }

    public void setUpdateManId(String updateManId) {
        this.updateManId = updateManId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}