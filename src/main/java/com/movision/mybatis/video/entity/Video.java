package com.movision.mybatis.video.entity;

import java.util.Date;

public class Video {
    private Integer id;

    private Integer postid;

    private String videoname;

    private String videourl;

    private Integer type;

    private Integer playnum;

    private Integer isrecommend;

    private Integer isbanner;

    private String bannerimgurl;

    private Integer orderid;

    private Date intime;

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

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname == null ? null : videoname.trim();
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl == null ? null : videourl.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPlaynum() {
        return playnum;
    }

    public void setPlaynum(Integer playnum) {
        this.playnum = playnum;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getIsbanner() {
        return isbanner;
    }

    public void setIsbanner(Integer isbanner) {
        this.isbanner = isbanner;
    }

    public String getBannerimgurl() {
        return bannerimgurl;
    }

    public void setBannerimgurl(String bannerimgurl) {
        this.bannerimgurl = bannerimgurl == null ? null : bannerimgurl.trim();
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
}