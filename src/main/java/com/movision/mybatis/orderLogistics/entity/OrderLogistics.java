package com.movision.mybatis.orderLogistics.entity;

import java.util.Date;

public class OrderLogistics {
    private Integer id;

    private String logisticsname;

    private Integer logisticstatue;

    private Date intime;

    private Integer isdel;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogisticsname() {
        return logisticsname;
    }

    public void setLogisticsname(String logisticsname) {
        this.logisticsname = logisticsname == null ? null : logisticsname.trim();
    }

    public Integer getLogisticstatue() {
        return logisticstatue;
    }

    public void setLogisticstatue(Integer logisticstatue) {
        this.logisticstatue = logisticstatue;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
}