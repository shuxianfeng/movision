package com.movision.mybatis.comment.entity;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {
    private Integer id;

    private Integer userid;

    private  Integer postid;

    private Integer pid;

    private String content;

    private Date intime;

    private Integer zansum;

    private String isdel;

    private Integer iscontribute;//是否为特邀嘉宾的评论：0否 1是

    private Integer status;//审核状态：0待审核 1审核通过 2审核不通过（iscontribute为1时不为空）

    private Integer isread;//是否已读 0否 1是

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getZansum() {
        return zansum;
    }

    public void setZansum(Integer zansum) {
        this.zansum = zansum;
    }

    public void setPostid(Integer postid){
        this.postid=postid;
    }

    public Integer getPostid(){
        return  postid;
    }

    public Integer getIscontribute() {
        return iscontribute;
    }

    public void setIscontribute(Integer iscontribute) {
        this.iscontribute = iscontribute;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}