package com.movision.mybatis.coupon.entity;

import java.util.Date;

public class CouponVo {
    private Integer id;

    private String title;//标题

    private String content;//详情

    private Integer type;//类型

    private String storename;//店铺名称

    private Integer statue;//状态

    private Date begintime;//开始时间

    private Date endtime;//结束时间

    private Date intime;//优惠券放券时间

    private Double tmoney;//金额

    private Double usemoney;//满多少可用

    private Integer isdel;//是否删除

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
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public Integer getStatue() {
        return statue;
    }

    public void setStatue(Integer statue) {
        this.statue = statue;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Double getTmoney() {
        return tmoney;
    }

    public void setTmoney(Double tmoney) {
        this.tmoney = tmoney;
    }

    public Double getUsemoney() {
        return usemoney;
    }

    public void setUsemoney(Double usemoney) {
        this.usemoney = usemoney;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}