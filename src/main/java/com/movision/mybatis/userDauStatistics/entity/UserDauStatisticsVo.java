package com.movision.mybatis.userDauStatistics.entity;

import java.util.Date;

public class UserDauStatisticsVo {
    private Integer id;

    private Date date;

    private Integer registenum;

    private Integer usersum;

    private Integer validsum;

    private Integer channel;

    private Date intime;

    private Integer regGather;//注册统计

    private Integer userGather;//日活统计

    private Integer valGather;//活跃统计

    public Integer getRegGather() {
        return regGather;
    }

    public void setRegGather(Integer regGather) {
        this.regGather = regGather;
    }

    public Integer getUserGather() {
        return userGather;
    }

    public void setUserGather(Integer userGather) {
        this.userGather = userGather;
    }

    public Integer getValGather() {
        return valGather;
    }

    public void setValGather(Integer valGather) {
        this.valGather = valGather;
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