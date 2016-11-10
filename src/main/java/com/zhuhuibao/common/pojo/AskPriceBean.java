package com.zhuhuibao.common.pojo;

import org.apache.commons.lang.StringUtils;

/**
 * Created by cxx on 2016/3/31 0031.
 */
public class AskPriceBean {

    String createid;

    String title;

    String productName;

    String content;

    String billurl;

    String endTime;

    String status;

    String statusName;

    String type;

    String typeName;

    String isTax;

    String isTaxName;

    String description;

    String companyName;

    String linkMan;

    String telephone;

    String email;

    String fcateName;

    String provinceName;

    String cityName;

    String areaName;

    Boolean isShow;

    String isCan;

    String period;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        if (StringUtils.isBlank(typeName) && StringUtils.isNotBlank(type)) {
            if ("1".equals(type)) {
                typeName = "公开询价";
            } else if ("2".equals(type)) {
                typeName = "定向询价";
            }
        }

        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getIsTax() {
        return isTax;
    }

    public void setIsTax(String isTax) {
        this.isTax = isTax;
    }

    public String getIsTaxName() {

        if (StringUtils.isBlank(isTaxName)) {
            isTaxName = "1".equals(getIsTax()) ? "含税报价" : "非含税报价";
        }

        return isTaxName;
    }

    public void setIsTaxName(String isTaxName) {
        this.isTaxName = isTaxName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getFcateName() {
        return fcateName;
    }

    public void setFcateName(String fcateName) {
        this.fcateName = fcateName;
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

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getBillurl() {
        return billurl;
    }

    public void setBillurl(String billurl) {
        this.billurl = billurl;
    }

    public String getIsCan() {
        return isCan;
    }

    public void setIsCan(String isCan) {
        this.isCan = isCan;
    }

    public String getCreateid() {
        return createid;
    }

    public void setCreateid(String createid) {
        this.createid = createid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAddress() {
        return getProvinceName() + getCityName() + getAreaName();
    }

}
