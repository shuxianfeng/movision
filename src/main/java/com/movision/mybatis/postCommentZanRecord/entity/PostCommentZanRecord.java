package com.movision.mybatis.postCommentZanRecord.entity;

import java.io.Serializable;
import java.util.Date;

public class PostCommentZanRecord implements Serializable {
    private Integer id;

    private Integer userid;

    private Integer commentid;

    private Date intime;

    private Integer isread;//是否已读  0否 1是

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}