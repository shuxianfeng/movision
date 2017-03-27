package com.movision.mybatis.couponShareRecord.entity;

import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/3/27 17:29
 */
public class CouponShareRecordVo {
    private Integer id;//分享id

    private Integer distributeid;//优惠券投放id

    private Integer userid;//分享用户id

    private Integer singlesharenum;//单次分享张数

    private Integer receivednum;//已领取张数

    private Integer restnum;//剩余优惠券张数

    private Date intime;//分享时间

    private Integer isdel;//是否删除：0 否 1 是

    private String title;//优惠券标题

    private String content;//优惠券内容

    private Date startdate;//优惠券生效日期

    private Date enddate;//优惠券失效日期

    private Double amount;//券金额（单张）

    private Double fullamount;//满多少可用

    private Integer scope;//使用范围：0 全场通用券 1 自营全 2 店铺券

    private Integer shopid;//店铺id（scope为2店铺券时不为空）

    private Integer channel;//获取渠道：0 店内领取 1 支付分享

    private String trasurl;//跳转的领取的H5页面url

    private String bannerurl;//分享领取页面的banner图url（channel为1时不为空）

    private String couponrule;//优惠券使用规则（投放说明、活动说明）

    private Integer type;//类型：0 满减券 1 代金券

    private Integer status;//优惠券状态：0 可领取 1 未开始 2 已结束 3 已抢光

    private String shopname;//店铺名称

    private Integer isHaveGet;//用户是否已领取过：0 未领取 1 已领取

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDistributeid() {
        return distributeid;
    }

    public void setDistributeid(Integer distributeid) {
        this.distributeid = distributeid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getSinglesharenum() {
        return singlesharenum;
    }

    public void setSinglesharenum(Integer singlesharenum) {
        this.singlesharenum = singlesharenum;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public Integer getIsHaveGet() {
        return isHaveGet;
    }

    public void setIsHaveGet(Integer isHaveGet) {
        this.isHaveGet = isHaveGet;
    }

    public String getTrasurl() {
        return trasurl;
    }

    public void setTrasurl(String trasurl) {
        this.trasurl = trasurl;
    }
}
