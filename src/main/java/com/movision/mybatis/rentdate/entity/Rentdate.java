package com.movision.mybatis.rentdate.entity;

import java.util.Date;

public class Rentdate {
    private Integer id;

    private Integer suborderid;

    private Integer cartid;

    private Date rentdate;

    private Date intime;

    public Integer getSuborderid() {
        return suborderid;
    }

    public void setSuborderid(Integer suborderid) {
        this.suborderid = suborderid;
    }

    public Integer getCartid() {
        return cartid;
    }

    public void setCartid(Integer cartid) {
        this.cartid = cartid;
    }

    public Date getRentdate() {
        return rentdate;
    }

    public void setRentdate(Date rentdate) {
        this.rentdate = rentdate;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}