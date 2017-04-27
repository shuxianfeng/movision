package com.movision.mybatis.pointRecord.entity;

import java.io.Serializable;

/**
 * 个人积分统计
 *
 * @Author zhuangyuhao
 * @Date 2017/3/2 11:17
 */
public class PersonPointStatistics implements Serializable {
    private Integer rewardCount;    //个人打赏总数
    private Integer postCount;      //个人发帖总数
    private Integer commentCount;   //个人评论总数
    private Integer shareCount;     //个人分享总数

    @Override
    public String toString() {
        return "PersonPointStatistics{" +
                "rewardCount=" + rewardCount +
                ", postCount=" + postCount +
                ", commentCount=" + commentCount +
                ", shareCount=" + shareCount +
                '}';
    }

    public void setRewardCount(Integer rewardCount) {
        this.rewardCount = rewardCount;
    }

    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getRewardCount() {

        return rewardCount;
    }

    public Integer getPostCount() {
        return postCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public PersonPointStatistics() {

    }

    public PersonPointStatistics(Integer rewardCount, Integer postCount, Integer commentCount, Integer shareCount) {

        this.rewardCount = rewardCount;
        this.postCount = postCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
    }
}
