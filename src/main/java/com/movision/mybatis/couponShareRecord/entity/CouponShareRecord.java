package com.movision.mybatis.couponShareRecord.entity;

import java.util.Date;

public class CouponShareRecord {
    private Integer id;

    private Integer distributeid;

    private Integer userid;

    private Integer singlesharenum;

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
}