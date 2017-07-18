package com.movision.mybatis.userRefreshRecord.entity;

/**
 * @Author zhanglei
 * @Date 2017/6/14 16:02
 */
public class UserRefreshRecord {

    private String id;

    private int userid;

    private int postid;

    private String crileid;

    private String intime;

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public String getCrileid() {
        return crileid;
    }

    public void setCrileid(String crileid) {
        this.crileid = crileid;
    }
}
