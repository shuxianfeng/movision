package com.movision.mybatis.userBehavior.entity;

import java.util.Date;

public class UserBehavior {
    private Integer id;

    private Integer userid;

    private Integer circle1;

    private Integer circle2;

    private Integer circle3;

    private Date intime;

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

    public Integer getCircle1() {
        return circle1;
    }

    public void setCircle1(Integer circle1) {
        this.circle1 = circle1;
    }

    public Integer getCircle2() {
        return circle2;
    }

    public void setCircle2(Integer circle2) {
        this.circle2 = circle2;
    }

    public Integer getCircle3() {
        return circle3;
    }

    public void setCircle3(Integer circle3) {
        this.circle3 = circle3;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}