package com.movision.mybatis.userDauStatistics.entity;

import java.util.Date;

public class UserDauStatistics {
    private Integer id;

    private Date date;

    private Integer registenum;

    private Integer usersum;

    private Integer validsum;

    private Integer channel;

    private Date intime;

    private String keeprate;//留存

    public String getKeeprate() {
        return keeprate;
    }

    public void setKeeprate(String keeprate) {
        this.keeprate = keeprate;
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

    public Integer getRegistenum() {
        return registenum;
    }

    public void setRegistenum(Integer registenum) {
        this.registenum = registenum;
    }

    public Integer getUsersum() {
        return usersum;
    }

    public void setUsersum(Integer usersum) {
        this.usersum = usersum;
    }

    public Integer getValidsum() {
        return validsum;
    }

    public void setValidsum(Integer validsum) {
        this.validsum = validsum;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}