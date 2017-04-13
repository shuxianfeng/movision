package com.movision.mybatis.activePart.entity;

import java.io.Serializable;
import java.util.Date;

public class ActivePart implements Serializable {
    private Integer id;

    private Integer postid;

    private Integer userid;

    private String title;

    private String email;

    private String videourl;

    private String introduction;

    private Date intime;

    private Double paymoney;

    private Double paymoneyying;

    private Integer paystatue;

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

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl == null ? null : videourl.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(Double paymoney) {
        this.paymoney = paymoney;
    }

    public Double getPaymoneyying() {
        return paymoneyying;
    }

    public void setPaymoneyying(Double paymoneyying) {
        this.paymoneyying = paymoneyying;
    }

    public Integer getPaystatue() {
        return paystatue;
    }

    public void setPaystatue(Integer paystatue) {
        this.paystatue = paystatue;
    }
}