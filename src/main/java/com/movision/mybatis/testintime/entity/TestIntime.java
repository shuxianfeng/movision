package com.movision.mybatis.testintime.entity;

import java.util.Date;

public class TestIntime {
    private Integer id;

    private Date intime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}