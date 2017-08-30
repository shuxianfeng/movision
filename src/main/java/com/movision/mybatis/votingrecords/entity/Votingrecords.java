package com.movision.mybatis.votingrecords.entity;

import java.util.Date;

public class Votingrecords {
    private Integer id;

    private Integer activeid;

    private Integer takenumber;

    private String name;

    private Date intime;
    private Integer takeid;

    public Integer getTakeid() {
        return takeid;
    }

    public void setTakeid(Integer takeid) {
        this.takeid = takeid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }

    public Integer getTakenumber() {
        return takenumber;
    }

    public void setTakenumber(Integer takenumber) {
        this.takenumber = takenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}