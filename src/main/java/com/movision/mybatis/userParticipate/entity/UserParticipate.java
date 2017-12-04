package com.movision.mybatis.userParticipate.entity;

import java.util.Date;

public class UserParticipate {
    private Integer id;

    private Date date;

    private Integer newpost;

    private Integer activepost;

    private Integer replynum;

    private Integer zannum;

    private Integer forwardnum;

    private Integer follownum;

    private Integer followdnum;

    private Date intime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNewpost() {
        return newpost;
    }

    public void setNewpost(Integer newpost) {
        this.newpost = newpost;
    }

    public Integer getActivepost() {
        return activepost;
    }

    public void setActivepost(Integer activepost) {
        this.activepost = activepost;
    }

    public Integer getReplynum() {
        return replynum;
    }

    public void setReplynum(Integer replynum) {
        this.replynum = replynum;
    }

    public Integer getZannum() {
        return zannum;
    }

    public void setZannum(Integer zannum) {
        this.zannum = zannum;
    }

    public Integer getForwardnum() {
        return forwardnum;
    }

    public void setForwardnum(Integer forwardnum) {
        this.forwardnum = forwardnum;
    }

    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    public Integer getFollowdnum() {
        return followdnum;
    }

    public void setFollowdnum(Integer followdnum) {
        this.followdnum = followdnum;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}