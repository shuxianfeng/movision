package com.movision.mybatis.user.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/8/7 16:06
 * 用户徽章实体
 */
public class UserBadge implements Serializable{

    private Integer postsum;//发帖数徽章

    private Integer commentsum;//评论数徽章

    private Integer zansum;//点赞数徽章

    private Integer invitesum;//邀请数徽章

    private Integer finishuserinfo;//完善个人资料

    private Integer essencesum;//精选徽章

    private Integer isdv;//大V徽章

    private Integer footprint;//足迹徽章

    private Integer rnauth;//实名认证徽章

    private Integer consume;//消费徽章

    public Integer getPostsum() {
        return postsum;
    }

    public void setPostsum(Integer postsum) {
        this.postsum = postsum;
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

    public Integer getInvitesum() {
        return invitesum;
    }

    public void setInvitesum(Integer invitesum) {
        this.invitesum = invitesum;
    }

    public Integer getFinishuserinfo() {
        return finishuserinfo;
    }

    public void setFinishuserinfo(Integer finishuserinfo) {
        this.finishuserinfo = finishuserinfo;
    }

    public Integer getEssencesum() {
        return essencesum;
    }

    public void setEssencesum(Integer essencesum) {
        this.essencesum = essencesum;
    }

    public Integer getIsdv() {
        return isdv;
    }

    public void setIsdv(Integer isdv) {
        this.isdv = isdv;
    }

    public Integer getFootprint() {
        return footprint;
    }

    public void setFootprint(Integer footprint) {
        this.footprint = footprint;
    }

    public Integer getRnauth() {
        return rnauth;
    }

    public void setRnauth(Integer rnauth) {
        this.rnauth = rnauth;
    }

    public Integer getConsume() {
        return consume;
    }

    public void setConsume(Integer consume) {
        this.consume = consume;
    }
}