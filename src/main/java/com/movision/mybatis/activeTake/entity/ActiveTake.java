package com.movision.mybatis.activeTake.entity;

import java.util.Date;

public class ActiveTake {
    private Integer id;

    private Integer postid;

    private String device;

    private Date intime;

    private Integer activeid;

    private Integer num;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device == null ? null : device.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }
}