package com.zhuhuibao.mybatis.expert.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class Achievement {
    private String id;

    private String createId;

    private String publishTime;

    private String updateTime;

    @ApiModelProperty(value="标题",required = true)
    private String title;

    @ApiModelProperty(value="系统分类",required = true)
    private String systemType;

    @ApiModelProperty(value="应用领域",required = true)
    private String useArea;

    @ApiModelProperty(value="单位名称")
    private String companyName;

    @ApiModelProperty(value="联系人",required = true)
    private String linkMan;

    @ApiModelProperty(value="联系电话",required = true)
    private String telephone;

    @ApiModelProperty(value="电子邮箱")
    private String email;

    @ApiModelProperty(value="省",required = true)
    private String province;

    @ApiModelProperty(value="市",required = true)
    private String city;

    @ApiModelProperty(value="区",required = true)
    private String area;

    @ApiModelProperty(value="详细地址",required = true)
    private String address;

    @ApiModelProperty(value="合作方式")
    private String cooperationType;

    private String status;

    private String is_deleted;

    @ApiModelProperty(value="成果簡介",required = true)
    private String introduce;

    private String provinceName;

    private String cityName;

    private String areaName;

    private String systemName;

    private String useAreaName;

    private String createName;

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

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCooperationType() {
        return cooperationType;
    }

    public void setCooperationType(String cooperationType) {
        this.cooperationType = cooperationType;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getUseAreaName() {
        return useAreaName;
    }

    public void setUseAreaName(String useAreaName) {
        this.useAreaName = useAreaName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }
}