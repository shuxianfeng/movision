package com.movision.mybatis.postHeatvalueRecord.entity;

import java.util.Date;

public class PostHeatvalueRecord {
    private Integer id;

    private Integer userid;

    private Integer postid;

    private Integer heatValue;

    private Integer isadd;

    private Integer type;

    private Date intime;

    private Integer isdel;

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

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getHeatValue() {
        return heatValue;
    }

    public void setHeatValue(Integer heatValue) {
        this.heatValue = heatValue;
    }

    public Integer getIsadd() {
        return isadd;
    }

    public void setIsadd(Integer isadd) {
        this.isadd = isadd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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