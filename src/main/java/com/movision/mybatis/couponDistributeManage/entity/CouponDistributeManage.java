package com.movision.mybatis.couponDistributeManage.entity;

import java.util.Date;

public class CouponDistributeManage {
    private Integer id;

    private String title;

    private String content;

    private Date startdate;

    private Date enddate;

    private Double amount;

    private Double fullamount;

    private Integer scope;

    private Integer shopid;

    private Integer channel;

    private String trasurl;

    private String bannerurl;

    private String couponrule;

    private Integer singlesharenum;

    private Integer type;

    private Integer putnum;

    private Double totalamount;

    private Integer receivednum;

    private Integer restnum;

    private Date intime;

    private Integer isdel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFullamount() {
        return fullamount;
    }

    public void setFullamount(Double fullamount) {
        this.fullamount = fullamount;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getSinglesharenum() {
        return singlesharenum;
    }

    public void setSinglesharenum(Integer singlesharenum) {
        this.singlesharenum = singlesharenum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPutnum() {
        return putnum;
    }

    public void setPutnum(Integer putnum) {
        this.putnum = putnum;
    }

    public Double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(Double totalamount) {
        this.totalamount = totalamount;
    }

    public Integer getReceivednum() {
        return receivednum;
    }

    public void setReceivednum(Integer receivednum) {
        this.receivednum = receivednum;
    }

    public Integer getRestnum() {
        return restnum;
    }

    public void setRestnum(Integer restnum) {
        this.restnum = restnum;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getTrasurl() {
        return trasurl;
    }

    public void setTrasurl(String trasurl) {
        this.trasurl = trasurl;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }

    public String getCouponrule() {
        return couponrule;
    }

    public void setCouponrule(String couponrule) {
        this.couponrule = couponrule;
    }
}