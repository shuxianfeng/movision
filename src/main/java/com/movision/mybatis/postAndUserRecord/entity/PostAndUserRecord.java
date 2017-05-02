package com.movision.mybatis.postAndUserRecord.entity;

import org.springframework.data.annotation.Id;

/**
 * @Author zhanglei
 * @Date 2017/4/27 19:35
 */
public class PostAndUserRecord {

    @Id
    private String id;

    private Integer userid;

    private Integer postid;

    private Integer crileid;

    private String  intime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getCrileid() {
        return crileid;
    }

    public void setCrileid(Integer crileid) {
        this.crileid = crileid;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }
}
