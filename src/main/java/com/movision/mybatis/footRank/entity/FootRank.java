package com.movision.mybatis.footRank.entity;

import java.util.Date;

public class FootRank {
    private Integer userid;

    private Integer footsum;

    private Date intime;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getFootsum() {
        return footsum;
    }

    public void setFootsum(Integer footsum) {
        this.footsum = footsum;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}