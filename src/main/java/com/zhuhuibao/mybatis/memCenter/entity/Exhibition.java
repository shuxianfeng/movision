package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

public class Exhibition {
    private String id;

    @ApiModelProperty(value="发布人ID")
    private String createid;

    @ApiModelProperty(value="发布人名称")
    private String createName;

    @ApiModelProperty(value="会展标题")
    private String title;

    @ApiModelProperty(value="发布时间")
    private String publishTime;

    @ApiModelProperty(value="更新时间")
    private String updateTime;

    @ApiModelProperty(value="开始时间")
    private String startDate;

    @ApiModelProperty(value="截止时间")
    private String endDate;

    @ApiModelProperty(value="省名称")
    private String provinceName;

    @ApiModelProperty(value="市名称")
    private String cityName;

    @ApiModelProperty(value="区名称")
    private String areaName;

    @ApiModelProperty(value="省")
    private String province;

    @ApiModelProperty(value="市")
    private String city;

    @ApiModelProperty(value="区")
    private String area;

    @ApiModelProperty(value="所属栏目")
    private String type;

    @ApiModelProperty(value="筑慧活动子栏目")
    private String subType;

    @ApiModelProperty(value="主办单位")
    private String company;

    @ApiModelProperty(value="详细地址")
    private String address;

    @ApiModelProperty(value="会展缩略图")
    private String imgUrl;

    @ApiModelProperty(value="审核状态：0：未审核；1：已审核；2：已拒绝")
    private String status;

    @ApiModelProperty(value="内容介绍")
    private String introduce;

    @ApiModelProperty(value="点击率")
    private String views;

    @ApiModelProperty(value="发布人类型：1：运营人员；2：会员",required = true)
    private String createrType;

    private String is_deleted;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getCreaterType() {
        return createrType;
    }

    public void setCreaterType(String createrType) {
        this.createrType = createrType;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}