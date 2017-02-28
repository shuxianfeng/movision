package com.movision.mybatis.goodsDiscount.entity;

import java.util.Date;

public class GoodsDiscount {
    private Integer id;

    private String name;

    private String content;

    private Date startdate;

    private Date enddate;

    private Integer isenrent;

    private Integer rentday;

    private Integer discount;

    private Integer orderid;

    private Date intime;

    private Integer isdel;

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

    public Integer getIsenrent() {
        return isenrent;
    }

    public void setIsenrent(Integer isenrent) {
        this.isenrent = isenrent;
    }

    public Integer getRentday() {
        return rentday;
    }

    public void setRentday(Integer rentday) {
        this.rentday = rentday;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
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
}