package com.movision.mybatis.post.entity;

import java.util.Date;

/**
 * 用于帖子列表
 *
 * @Author zhurui
 * @Date 2017/2/7 9:53
 */
public class PostList {

    private String title;//标题

    private String nickname;//发帖人

    private Integer collectsum;//收藏数

    private Integer share;//分享数

    private Integer commentsum;//评论数

    private Integer zansum;//点赞数

    private Integer rewarded;//打赏积分

    private Integer accusation;//举报次数

    private Integer isessence;//是否为精选

    private Date istime;//精选时间

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCollectsum() {
        return collectsum;
    }

    public void setCollectsum(Integer collectsum) {
        this.collectsum = collectsum;
    }

    public Integer getShare() {
        return share;
    }

    public void setShare(Integer share) {
        this.share = share;
    }

    public Integer getCommentsum() {
        return commentsum;
    }

    public void setCommentsum(Integer commentsum) {
        this.commentsum = commentsum;
    }

    public Integer getZansum() {
        return zansum;
    }

    public void setZansum(Integer zansum) {
        this.zansum = zansum;
    }

    public Integer getRewarded() {
        return rewarded;
    }

    public void setRewarded(Integer rewarded) {
        this.rewarded = rewarded;
    }

    public Integer getAccusation() {
        return accusation;
    }

    public void setAccusation(Integer accusation) {
        this.accusation = accusation;
    }

    public Integer getIsessence() {
        return isessence;
    }

    public void setIsessence(Integer isessence) {
        this.isessence = isessence;
    }

    public Date getIstime() {
        return istime;
    }

    public void setIstime(Date istime) {
        this.istime = istime;
    }
}
