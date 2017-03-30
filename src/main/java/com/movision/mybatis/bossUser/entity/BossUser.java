package com.movision.mybatis.bossUser.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "boss用户", description = "boss用户")
public class BossUser implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "真实姓名")
    private String name;

    private String phone;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "是否是超级管理员：0 否 1 是")
    private Integer issuper;
    @ApiModelProperty(value = "账号状态: 0 正常 1 冻结")
    private Integer status;
    @ApiModelProperty(value = "是否被删除：0 未删除 1 已删除")
    private Integer isdel;
    @ApiModelProperty(value = "账号创建时间")
    private Date createtime;
    @ApiModelProperty(value = "上次登录时间")
    private Date afterlogintime;
    @ApiModelProperty(value = "本次登录时间")
    private Date beforelogintime;
    @ApiModelProperty(value = "是否是圈主 0否 1是")
    private Integer iscircle;
    @ApiModelProperty(value = "是否是圈子管理员 0否 1是")
    private Integer circlemanagement;
    @ApiModelProperty(value = "是否是特约嘉宾 0否 1是")
    private Integer contributing;
    @ApiModelProperty(value = "是否是普通管理员 0否 1是")
    private Integer common;

    public Integer getIscircle() {
        return iscircle;
    }

    public void setIscircle(Integer iscircle) {
        this.iscircle = iscircle;
    }

    public Integer getCirclemanagement() {
        return circlemanagement;
    }

    public void setCirclemanagement(Integer circlemanagement) {
        this.circlemanagement = circlemanagement;
    }

    public Integer getContributing() {
        return contributing;
    }

    public void setContributing(Integer contributing) {
        this.contributing = contributing;
    }

    public Integer getCommon() {
        return common;
    }

    public void setCommon(Integer common) {
        this.common = common;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getIssuper() {
        return issuper;
    }

    public void setIssuper(Integer issuper) {
        this.issuper = issuper;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getAfterlogintime() {
        return afterlogintime;
    }

    public void setAfterlogintime(Date afterlogintime) {
        this.afterlogintime = afterlogintime;
    }

    public Date getBeforelogintime() {
        return beforelogintime;
    }

    public void setBeforelogintime(Date beforelogintime) {
        this.beforelogintime = beforelogintime;
    }

    @Override
    public String toString() {
        return "BossUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", issuper=" + issuper +
                ", status=" + status +
                ", isdel=" + isdel +
                ", createtime=" + createtime +
                ", afterlogintime=" + afterlogintime +
                ", beforelogintime=" + beforelogintime +
                '}';
    }
}