package com.zhuhuibao.mybatis.oms.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @Description 甲方信息
 * @author penglong
 * @createDate 2016-05-13
 */
@ApiModel(value = "甲方信息",description = "甲方信息")
public class ProjectPartyA {
    @ApiModelProperty(value = "甲方信息ID，主键")
    private Integer id;
    @ApiModelProperty(value = "项目ID")
    private Long projectid;
    @ApiModelProperty(value = "单位类型")
    private String deptType;
    @ApiModelProperty(value = "单位名称")
    private String name;
    @ApiModelProperty(value = "联系人")
    private String linkman;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "座机")
    private String telephone;
    @ApiModelProperty(value = "传真")
    private String fax;
    @ApiModelProperty(value = "地址")
    private String address;
    @ApiModelProperty(value = "备注")
    private String note;
    @ApiModelProperty(value = "删除标识：1删除，0不删除")
    private Boolean is_deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getProjectid() {
        return projectid;
    }

    public void setProjectid(Long projectid) {
        this.projectid = projectid;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType == null ? null : deptType.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Boolean getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Boolean is_deleted) {
        this.is_deleted = is_deleted;
    }
}