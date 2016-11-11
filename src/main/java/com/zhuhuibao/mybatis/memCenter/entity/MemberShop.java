package com.zhuhuibao.mybatis.memCenter.entity;

import java.util.List;

public class MemberShop {
    private Integer id;

    private String shopName;

    private Integer companyId;

    private String companyName;

    private String companyAccount;

    private String bannerUrl;

    private String updateTime;

    private Integer opreatorId;

    private String status;

    private String reason;

    private String mobileBannerUrlF;

    private String mobileBannerUrlS;

    private String mobileBannerUrlT;

    private List<String> bannerList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAccount() {
        return companyAccount;
    }

    public void setCompanyAccount(String companyAccount) {
        this.companyAccount = companyAccount == null ? null : companyAccount.trim();
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl == null ? null : bannerUrl.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOpreatorId() {
        return opreatorId;
    }

    public void setOpreatorId(Integer opreatorId) {
        this.opreatorId = opreatorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMobileBannerUrlF() {
        return mobileBannerUrlF;
    }

    public void setMobileBannerUrlF(String mobileBannerUrlF) {
        this.mobileBannerUrlF = mobileBannerUrlF;
    }

    public String getMobileBannerUrlS() {
        return mobileBannerUrlS;
    }

    public void setMobileBannerUrlS(String mobileBannerUrlS) {
        this.mobileBannerUrlS = mobileBannerUrlS;
    }

    public String getMobileBannerUrlT() {
        return mobileBannerUrlT;
    }

    public void setMobileBannerUrlT(String mobileBannerUrlT) {
        this.mobileBannerUrlT = mobileBannerUrlT;
    }

    public List<String> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<String> bannerList) {
        this.bannerList = bannerList;
    }
}