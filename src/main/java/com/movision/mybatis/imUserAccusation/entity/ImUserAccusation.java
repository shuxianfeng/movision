package com.movision.mybatis.imUserAccusation.entity;

import java.util.Date;

public class ImUserAccusation {
    private Integer id;

    private String fromid;

    private String toid;

    private String comment;

    private Integer type;

    private Date intime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public String getFromid() {

        return fromid;
    }

    public String getToid() {
        return toid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}