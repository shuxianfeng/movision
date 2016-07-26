package com.zhuhuibao.mybatis.common.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SysJoinus {
    private Integer id;

    @ApiModelProperty(value = "真实姓名",required = true)
    private String realName;

    @ApiModelProperty(value = "手机号码",required = true)
    private String mobile;

    @ApiModelProperty("QQ")
    private String qq;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("处理状态")
    private String status;

    @ApiModelProperty("处理备注")
    private String remark;

    @ApiModelProperty("图形验证码")
    private String imgCode;

    public String getImgCode() {
        return imgCode;
    }

    public void setImgCode(String imgCode) {
        this.imgCode = imgCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}