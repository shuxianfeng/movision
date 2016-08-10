package com.zhuhuibao.mybatis.memCenter.entity;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class AskPrice {
    private Long id;

    @ApiModelProperty(value="发布人ID")
    private String createid;

    @ApiModelProperty(value="询价标题")
    private String title;

    @ApiModelProperty(value="状态 1：询价中，2：结束")
    private Byte status;

    @ApiModelProperty(value="询价类型   1：公开询价，2：定向询价")
    private Byte type;

    @ApiModelProperty(value="产品询价保存的内容")
    private String content;

    @ApiModelProperty(value="询价单的url")
    private String billurl;

    @ApiModelProperty(value="询价发布时间")
    private String publishTime;

    @ApiModelProperty(value="询价截止时间")
    private String endTime;

    @ApiModelProperty(value="交货省")
    private String provinceCode;

    @ApiModelProperty(value="交货市")
    private String cityCode;

    @ApiModelProperty(value="交货区")
    private String areaCode;

    @ApiModelProperty(value="一级分类ID")
    private String fcateid;

    @ApiModelProperty(value="品牌ID")
    private String brandid;

    @ApiModelProperty(value="品牌名称")
    private String brandName;

    @ApiModelProperty(value="产品ID")
    private String productid;

    @ApiModelProperty(value="产品/服务名称")
    private String productName;

    @ApiModelProperty(value="供应商ID。多个用逗号隔开。")
    private String supplierid;

    @ApiModelProperty(value="是否含税 0:不含税，1：含税")
    private String isTax;

    @ApiModelProperty(value="备货周期")
    private Integer period;

    @ApiModelProperty(value="说明")
    private String description;

    @ApiModelProperty(value="1：显示我的联系方式，0：不显示")
    private Boolean isShow;

    @ApiModelProperty(value="公司名称")
    private String companyName;

    @ApiModelProperty(value="公司联系人")
    private String linkMan;

    @ApiModelProperty(value="联系电话")
    private String telephone;

    @ApiModelProperty(value="电子邮箱")
    private String email;

    private String isCan;

    private String count;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        this.title = title == null ? null : title.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode == null ? null : areaCode.trim();
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid == null ? null : supplierid.trim();
    }

    public String getIsTax() {
        return isTax;
    }

    public void setIsTax(String isTax) {
        this.isTax = isTax == null ? null : isTax.trim();
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan == null ? null : linkMan.trim();
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

    public String getFcateid() {
        return fcateid;
    }

    public void setFcateid(String fcateid) {
        this.fcateid = fcateid;
    }

    public String getBillurl() {
        return billurl;
    }

    public void setBillurl(String billurl) {
        this.billurl = billurl;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getIsCan() {
        return isCan;
    }

    public void setIsCan(String isCan) {
        this.isCan = isCan;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}