package com.movision.mybatis.logisticsfeeCalculateRule.entity;

import java.io.Serializable;
import java.util.Date;

public class LogisticsfeeCalculateRule implements Serializable {
    private Integer shopid;

    private Double startprice;

    private Double startdistance;

    private Double beyondbilling;

    private Date intime;

    private Double capping;

    public Double getCapping() {
        return capping;
    }

    public void setCapping(Double capping) {
        this.capping = capping;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Double getStartprice() {
        return startprice;
    }

    public void setStartprice(Double startprice) {
        this.startprice = startprice;
    }

    public Double getStartdistance() {
        return startdistance;
    }

    public void setStartdistance(Double startdistance) {
        this.startdistance = startdistance;
    }

    public Double getBeyondbilling() {
        return beyondbilling;
    }

    public void setBeyondbilling(Double beyondbilling) {
        this.beyondbilling = beyondbilling;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}