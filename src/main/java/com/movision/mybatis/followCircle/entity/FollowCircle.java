package com.movision.mybatis.followCircle.entity;

import java.io.Serializable;

public class FollowCircle implements Serializable {
    private Integer id;

    private Integer userid;

    private Integer circleid;

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

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }
}