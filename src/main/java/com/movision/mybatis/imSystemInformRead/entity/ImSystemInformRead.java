package com.movision.mybatis.imSystemInformRead.entity;

import java.util.Date;

public class ImSystemInformRead {
    private Integer id;

    private Integer userid;

    private Date intime;

    private String informIdentity;

    private Integer isread;

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getInformIdentity() {
        return informIdentity;
    }

    public void setInformIdentity(String informIdentity) {
        this.informIdentity = informIdentity == null ? null : informIdentity.trim();
    }
}