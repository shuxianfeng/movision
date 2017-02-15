package com.movision.mybatis.bossOrders.entity;

import java.util.Date;

public class BossOrders {
    private Integer id;

    private String ordernumber;

    private Integer userid;

    private Integer addressid;

    private Integer takeway;

    private Integer position;

    private Integer status;

    private String bill;

    private Double money;

    private Double sendmoney;

    private Integer isdiscount;

    private String couponsid;

    private Double discouponmoney;

    private Double dispointmoney;

    private Double realmoney;

    private Date intime;

    private Integer paytype;

    private Date paytime;

    private String paycode;

    private String remark;

    private Integer isdel;

    private Integer logictisid;

    public Integer getLogictisid() {
        return logictisid;
    }

    public void setLogictisid(Integer logictisid) {
        this.logictisid = logictisid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber == null ? null : ordernumber.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getAddressid() {
        return addressid;
    }

    public void setAddressid(Integer addressid) {
        this.addressid = addressid;
    }

    public Integer getTakeway() {
        return takeway;
    }

    public void setTakeway(Integer takeway) {
        this.takeway = takeway;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill == null ? null : bill.trim();
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getSendmoney() {
        return sendmoney;
    }

    public void setSendmoney(Double sendmoney) {
        this.sendmoney = sendmoney;
    }

    public Integer getIsdiscount() {
        return isdiscount;
    }

    public void setIsdiscount(Integer isdiscount) {
        this.isdiscount = isdiscount;
    }

    public String getCouponsid() {
        return couponsid;
    }

    public void setCouponsid(String couponsid) {
        this.couponsid = couponsid == null ? null : couponsid.trim();
    }

    public Double getDiscouponmoney() {
        return discouponmoney;
    }

    public void setDiscouponmoney(Double discouponmoney) {
        this.discouponmoney = discouponmoney;
    }

    public Double getDispointmoney() {
        return dispointmoney;
    }

    public void setDispointmoney(Double dispointmoney) {
        this.dispointmoney = dispointmoney;
    }

    public Double getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(Double realmoney) {
        this.realmoney = realmoney;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public String getPaycode() {
        return paycode;
    }

    public void setPaycode(String paycode) {
        this.paycode = paycode == null ? null : paycode.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}