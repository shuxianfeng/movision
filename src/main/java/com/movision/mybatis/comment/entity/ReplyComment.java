package com.movision.mybatis.comment.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/18 14:39
 */
public class ReplyComment implements Serializable {

    private Integer id; //评论id
    private Integer userid; //评论人id
    private String photo;   //评论人头像
    private String nickname;    //昵称
    private String replyContent;    //评论人回复的内容
    private Date intime;

    private String originContent;   //原始评论的内容
    private Integer originCommentId;    //原始评论id

    private Integer postid; //原生评论对应的帖子id

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getPostid() {

        return postid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setOriginContent(String originContent) {
        this.originContent = originContent;
    }

    public void setOriginCommentId(Integer originCommentId) {
        this.originCommentId = originCommentId;
    }

    public Integer getId() {

        return id;
    }

    public Integer getUserid() {
        return userid;
    }

    public String getPhoto() {
        return photo;
    }

    public String getNickname() {
        return nickname;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public Date getIntime() {
        return intime;
    }

    public String getOriginContent() {
        return originContent;
    }

    public Integer getOriginCommentId() {
        return originCommentId;
    }

    @Override
    public String toString() {
        return "ReplyComment{" +
                "id=" + id +
                ", userid=" + userid +
                ", photo='" + photo + '\'' +
                ", nickname='" + nickname + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", intime=" + intime +
                ", originContent='" + originContent + '\'' +
                ", originCommentId=" + originCommentId +
                ", postid=" + postid +
                '}';
    }
}
