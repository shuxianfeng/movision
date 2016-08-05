package com.zhuhuibao.mybatis.memCenter.entity;

import java.util.Date;

public class MemRealCheck {
    private Long id;

    private String mobile;

    private String email;

    private Date updateTime;

    private Integer status;

    private String enterpriseName;

    private String companyBusinessLicenseImg;

    private String personRealName;

    private String nickname;

    private String personCompanyType;

    private String personIdentifyCard;

    private String personIDFrontImgUrl;

    private String personIDBackImgUrl;

    private String reason;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName == null ? null : enterpriseName.trim();
    }

    public String getCompanyBusinessLicenseImg() {
        return companyBusinessLicenseImg;
    }

    public void setCompanyBusinessLicenseImg(String companyBusinessLicenseImg) {
        this.companyBusinessLicenseImg = companyBusinessLicenseImg == null ? null : companyBusinessLicenseImg.trim();
    }

    public String getPersonRealName() {
        return personRealName;
    }

    public void setPersonRealName(String personRealName) {
        this.personRealName = personRealName == null ? null : personRealName.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getPersonCompanyType() {
        return personCompanyType;
    }

    public void setPersonCompanyType(String personCompanyType) {
        this.personCompanyType = personCompanyType == null ? null : personCompanyType.trim();
    }

    public String getPersonIdentifyCard() {
        return personIdentifyCard;
    }

    public void setPersonIdentifyCard(String personIdentifyCard) {
        this.personIdentifyCard = personIdentifyCard == null ? null : personIdentifyCard.trim();
    }

    public String getPersonIDFrontImgUrl() {
        return personIDFrontImgUrl;
    }

    public void setPersonIDFrontImgUrl(String personIDFrontImgUrl) {
        this.personIDFrontImgUrl = personIDFrontImgUrl == null ? null : personIDFrontImgUrl.trim();
    }

    public String getPersonIDBackImgUrl() {
        return personIDBackImgUrl;
    }

    public void setPersonIDBackImgUrl(String personIDBackImgUrl) {
        this.personIDBackImgUrl = personIDBackImgUrl == null ? null : personIDBackImgUrl.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}