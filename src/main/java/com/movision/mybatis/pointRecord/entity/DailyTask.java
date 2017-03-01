package com.movision.mybatis.pointRecord.entity;

import java.io.Serializable;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/1 11:42
 */
public class DailyTask implements Serializable {
    //签到
    private Integer signCount;
    private Boolean sign;
    //打赏
    private Integer rewardCount;
    private Boolean reward;
    //发帖
    private Integer postCount;
    private Boolean post;
    //评论
    private Integer commentCount;
    private Boolean comment;
    //分享
    private Integer shareCount;
    private Boolean share;
    //首页精选
    private Integer indexSelectedCount;
    private Boolean indexSelected;
    //圈子精选
    private Integer circleSelectedCount;
    private Boolean circleSelected;

    public DailyTask() {
    }

    public DailyTask(Integer signCount, Boolean sign, Integer rewardCount, Boolean reward, Integer postCount, Boolean post, Integer commentCount, Boolean comment, Integer shareCount, Boolean share, Integer indexSelectedCount, Boolean indexSelected, Integer circleSelectedCount, Boolean circleSelected) {
        this.signCount = signCount;
        this.sign = sign;
        this.rewardCount = rewardCount;
        this.reward = reward;
        this.postCount = postCount;
        this.post = post;
        this.commentCount = commentCount;
        this.comment = comment;
        this.shareCount = shareCount;
        this.share = share;
        this.indexSelectedCount = indexSelectedCount;
        this.indexSelected = indexSelected;
        this.circleSelectedCount = circleSelectedCount;
        this.circleSelected = circleSelected;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public void setSign(Boolean sign) {
        this.sign = sign;
    }

    public void setRewardCount(Integer rewardCount) {
        this.rewardCount = rewardCount;
    }

    public void setReward(Boolean reward) {
        this.reward = reward;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public void setPost(Boolean post) {
        this.post = post;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setComment(Boolean comment) {
        this.comment = comment;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public void setIndexSelectedCount(Integer indexSelectedCount) {
        this.indexSelectedCount = indexSelectedCount;
    }

    public void setIndexSelected(Boolean indexSelected) {
        this.indexSelected = indexSelected;
    }

    public void setCircleSelectedCount(Integer circleSelectedCount) {
        this.circleSelectedCount = circleSelectedCount;
    }

    public void setCircleSelected(Boolean circleSelected) {
        this.circleSelected = circleSelected;
    }

    public Integer getSignCount() {

        return signCount;
    }

    public Boolean getSign() {
        return sign;
    }

    public Integer getRewardCount() {
        return rewardCount;
    }

    public Boolean getReward() {
        return reward;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public Boolean getPost() {
        return post;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Boolean getComment() {
        return comment;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public Boolean getShare() {
        return share;
    }

    public Integer getIndexSelectedCount() {
        return indexSelectedCount;
    }

    public Boolean getIndexSelected() {
        return indexSelected;
    }

    public Integer getCircleSelectedCount() {
        return circleSelectedCount;
    }

    public Boolean getCircleSelected() {
        return circleSelected;
    }

    @Override
    public String toString() {
        return "DailyTask{" +
                "signCount=" + signCount +
                ", sign=" + sign +
                ", rewardCount=" + rewardCount +
                ", reward=" + reward +
                ", postCount=" + postCount +
                ", post=" + post +
                ", commentCount=" + commentCount +
                ", comment=" + comment +
                ", shareCount=" + shareCount +
                ", share=" + share +
                ", indexSelectedCount=" + indexSelectedCount +
                ", indexSelected=" + indexSelected +
                ", circleSelectedCount=" + circleSelectedCount +
                ", circleSelected=" + circleSelected +
                '}';
    }
}
