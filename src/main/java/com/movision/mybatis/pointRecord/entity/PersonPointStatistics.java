package com.movision.mybatis.pointRecord.entity;

/**
 * 个人历史积分统计
 *
 * @Author zhuangyuhao
 * @Date 2017/3/2 11:17
 */
public class PersonPointStatistics {
    private Integer rewardCount;    //个人历史打赏总数
    private Integer postCount;  //个人历史发帖总数
    private Integer commentCount;   //个人历史评论总数
    private Integer shareCount; //个人历史分享总数

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
