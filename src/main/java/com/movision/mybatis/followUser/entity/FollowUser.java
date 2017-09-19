package com.movision.mybatis.followUser.entity;

import java.util.Date;

public class FollowUser {
    private Integer id;

    private Integer userid;

    private Integer interestedusers;

    private Date intime;

    private Integer isread;

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Integer getIsread() {

        return isread;
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

    public Integer getInterestedusers() {
        return interestedusers;
    }

    public void setInterestedusers(Integer interestedusers) {
        this.interestedusers = interestedusers;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}