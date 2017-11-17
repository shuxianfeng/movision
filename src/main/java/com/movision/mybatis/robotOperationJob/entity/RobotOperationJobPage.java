package com.movision.mybatis.robotOperationJob.entity;

import java.util.Date;

/**
 * 机器人任务列表   分页使用
 *
 * @Author zhuangyuhao
 * @Date 2017/11/1 11:42
 */
public class RobotOperationJobPage {

    private Integer id;

    private Integer userid;

    private Integer postid;

    private Integer type;

    private Integer status;

    private Integer count;

    private Date intime;

    private Date endtime;

    private Integer batch;

    private Integer number;

    private Integer theme;

    /**
     * 评论类型 ：0：普通 1：专业摄影 2：风光 3：人像 4：诗词 5：段子
     */
    private Integer commentType;

    /**
     * 被关注的用户名称
     */
    private String followedUser;

    /**
     * 被操作的帖子标题
     */
    private String title;

    /**
     * 被操作的帖子的作者
     */
    private String author;


    /**
     * 是否立即执行（0：否， 1：是）
     */
    private Integer immediate;


    public void setImmediate(Integer immediate) {
        this.immediate = immediate;
    }

    public Integer getImmediate() {

        return immediate;
    }

    public void setCommentType(Integer commentType) {
        this.commentType = commentType;
    }

    public Integer getCommentType() {

        return commentType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public void setFollowedUser(String followedUser) {
        this.followedUser = followedUser;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public Integer getId() {

        return id;
    }

    public Integer getUserid() {
        return userid;
    }

    public Integer getPostid() {
        return postid;
    }

    public Integer getType() {
        return type;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCount() {
        return count;
    }

    public Date getIntime() {
        return intime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public Integer getBatch() {
        return batch;
    }

    public Integer getNumber() {
        return number;
    }

    public Integer getTheme() {
        return theme;
    }

    public String getFollowedUser() {
        return followedUser;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "RobotOperationJobPage{" +
                "id=" + id +
                ", userid=" + userid +
                ", postid=" + postid +
                ", type=" + type +
                ", status=" + status +
                ", count=" + count +
                ", intime=" + intime +
                ", endtime=" + endtime +
                ", batch=" + batch +
                ", number=" + number +
                ", theme=" + theme +
                ", commentType=" + commentType +
                ", followedUser='" + followedUser + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", immediate=" + immediate +
                '}';
    }
}
