package com.movision.mybatis.userParticipate.entity;

import java.util.Date;

public class UserParticipateVo {
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

    private Integer newpGather;//新帖汇总

    private Integer activeGather;//活跃汇总

    private Integer replyGather;//回复量汇总

    private Integer zanGather;//赞汇总

    private Integer forwardGather;//转发汇总

    private Integer followGather;//申请关注汇总

    private Integer followdGather;//互相关注汇总

    public Integer getNewpGather() {
        return newpGather;
    }

    public void setNewpGather(Integer newpGather) {
        this.newpGather = newpGather;
    }

    public Integer getActiveGather() {
        return activeGather;
    }

    public void setActiveGather(Integer activeGather) {
        this.activeGather = activeGather;
    }

    public Integer getReplyGather() {
        return replyGather;
    }

    public void setReplyGather(Integer replyGather) {
        this.replyGather = replyGather;
    }

    public Integer getZanGather() {
        return zanGather;
    }

    public void setZanGather(Integer zanGather) {
        this.zanGather = zanGather;
    }

    public Integer getForwardGather() {
        return forwardGather;
    }

    public void setForwardGather(Integer forwardGather) {
        this.forwardGather = forwardGather;
    }

    public Integer getFollowGather() {
        return followGather;
    }

    public void setFollowGather(Integer followGather) {
        this.followGather = followGather;
    }

    public Integer getFollowdGather() {
        return followdGather;
    }

    public void setFollowdGather(Integer followdGather) {
        this.followdGather = followdGather;
    }

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