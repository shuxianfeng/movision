package com.movision.mybatis.comment.entity;

import java.util.Date;
import java.util.List;

public class CommentVo {
    private Integer id;

    private Integer userid;

    private Integer pid;

    private  Integer postid;

    private String content;

    private Date intime;

    private Integer zansum;

    private String phone;

    private String nickname;

    private String photo;

    private String isdel;

    private Integer isZan;//该用户是否已赞该帖子/活动 0 否 1 是

    private List<CommentVo> soncomment;

    public List<CommentVo> getSoncomment() {
        return soncomment;
    }

    public void setSoncomment(List<CommentVo> soncomment) {
        this.soncomment = soncomment;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    private List<CommentVo> vo;

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
        this.pid=pid;
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

    public void setPhone(String phone){
        this.phone=phone;
    }

    public String getPhone(){
        return phone;
    }

    public void setNickname(String nickname){
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public void setPhoto(String photo){
        this.photo=photo;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPostid(Integer postid){
        this.postid=postid;
    }

    public Integer getPostid(){
        return  postid;
    }

    public void addVo(List<CommentVo> vo){
        this.vo = vo;
    }

    public List<CommentVo> getVo(){
        return vo;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
    }
}