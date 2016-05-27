package com.zhuhuibao.mybatis.techtrain.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 技术合作：技术成果，技术需求
 * @author  penglong
 * @create 2016-05-27
 */
@ApiModel(value = "技术合作：技术成果，需求",description = "技术合作：技术成果和需求")
public class TechCooperation {
    @ApiModelProperty(value = "技术合作ID，主键")
    private Long id;
    @ApiModelProperty(value = "创建者ID")
    private Long createID;
    @ApiModelProperty(value = "发布时间")
    private Date publishTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "系统分类")
    private Integer systemCategory;
    @ApiModelProperty(value = "应用领域")
    private Integer applicationArea;
    @ApiModelProperty(value = "应用领域")
    private String linkman;

    private String companyName;

    private String telephone;

    private String email;

    private String province;

    private String city;

    private String area;

    private String address;

    private String cooperation;

    private Integer type;

    private Integer status;

    private Long views;

    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateID() {
        return createID;
    }

    public void setCreateID(Long createID) {
        this.createID = createID;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(Integer systemCategory) {
        this.systemCategory = systemCategory;
    }

    public Integer getApplicationArea() {
        return applicationArea;
    }

    public void setApplicationArea(Integer applicationArea) {
        this.applicationArea = applicationArea;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation == null ? null : cooperation.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}