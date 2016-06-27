package com.zhuhuibao.mybatis.tech.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

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
    private String publishTime;
    @ApiModelProperty(value = "修改时间")
    private String updateTime;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "系统分类")
    private Integer systemCategory;
    @ApiModelProperty(value = "应用领域")
    private Integer applicationArea;
    @ApiModelProperty(value = "联系人")
    private String linkman;
    @ApiModelProperty(value = "单位名称")
    private String companyName;
    @ApiModelProperty(value = "联系电话")
    private String telephone;
    @ApiModelProperty(value = "邮箱地址")
    private String email;
    @ApiModelProperty(value = "省代码")
    private String province;
    @ApiModelProperty(value = "市代码")
    private String city;
    @ApiModelProperty(value = "区域代码")
    private String area;
    @ApiModelProperty(value = "详细地址")
    private String address;
    @ApiModelProperty(value = "合作方式")
    private String cooperation;
    @ApiModelProperty(value = "类型：1:技术成果，2：技术需求")
    private Integer type;
    @ApiModelProperty(value = "状态：1：待审核，2：已审核，3：拒绝，4：删除")
    private Integer status;
    @ApiModelProperty(value = "点击率")
    private Long views;
    @ApiModelProperty(value = "成果简介")
    private String notes;
    @ApiModelProperty(value = "成果简介")
    private String reason;

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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}