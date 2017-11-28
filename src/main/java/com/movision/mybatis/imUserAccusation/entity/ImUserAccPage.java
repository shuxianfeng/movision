package com.movision.mybatis.imUserAccusation.entity;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/28 10:09
 */
public class ImUserAccPage {

    private Integer id;

    private String fromid;
    private String fromName;

    private String toid;
    private String toName;

    private String comment;

    private Integer type;

    private Date intime;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }


    public void setToid(String toid) {
        this.toid = toid;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getId() {

        return id;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromName() {

        return fromName;
    }

    public String getToid() {
        return toid;
    }

    public String getToName() {
        return toName;
    }

    public String getComment() {
        return comment;
    }

    public Integer getType() {
        return type;
    }

    public Date getIntime() {
        return intime;
    }
}
