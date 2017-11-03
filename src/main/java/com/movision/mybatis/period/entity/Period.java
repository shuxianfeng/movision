package com.movision.mybatis.period.entity;

import java.io.Serializable;
import java.util.Date;

public class Period implements Serializable {
    private Integer id;

    private Integer postid; //活动id

    private Date begintime;//开始时间

    private Date endtime;//结束时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Date getBegintime() {
        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}