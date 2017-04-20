package com.movision.mybatis.comment.entity;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.L;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommentVo implements Serializable {
    private Integer id;
    private Integer userid;
    private Integer pid;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private  Integer postid;

    private String content;

    private Date intime;

    private Integer zansum;

    private String phone;

    private String title;

    private String postcontent;

    private Integer activetype;

    private Integer type;

    private String coverimg;

    private Integer isactive;

    private String nickname;

    private Integer level;//用户等级：0 普通用户 >=1为大V

    private String photo;

    private String isdel;

    private Integer iscontribute;//是否为特邀嘉宾的评论：0否 1是

    private Integer status;//审核状态：0待审核 1审核通过 2审核不通过（iscontribute为1时不为空）

    private Integer isZan;//该用户是否已赞该帖子/活动 0 否 1 是

    private List<CommentVo> soncomment;

    private CommentVo vo;

    private List<Post> post;

    private List<CommentVo> commentVos;

    private Integer isread;//是否已读 0否 1是

    public Integer getIsread() {
        return isread;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public List<Post> getPost() {
        return post;
    }

    public void setPost(List<Post> post) {
        this.post = post;
    }

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public Integer getActivetype() {
        return activetype;
    }

    public void setActivetype(Integer activetype) {
        this.activetype = activetype;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }

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

    public void addVo(CommentVo vo) {
        this.vo = vo;
    }

    public CommentVo getVo() {
        return vo;
    }

    public Integer getIsZan() {
        return isZan;
    }

    public void setIsZan(Integer isZan) {
        this.isZan = isZan;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setVo(CommentVo vo) {
        this.vo = vo;
    }


    public List<CommentVo> getCommentVos() {
        return commentVos;
    }

    public void setCommentVos(List<CommentVo> commentVos) {
        this.commentVos = commentVos;
    }
}