package com.movision.mybatis.bossIndex.entity;

import java.io.Serializable;

/**
 * @Author zhurui
 * @Date 2017/3/11 15:31
 */
public class IndexTodayDetails implements Serializable {

    private String newPost;//今日新帖

    private String newMember;//今日新注册会员

    private String newActivityApply;//今日活动报名数

    private String newOrderForm;//;今日订单数

    private String applyForNotAudit;//申请加V未审核

    private Integer yesterdayPost;//昨日帖子

    private Integer yesterdayOrders;//昨日订单

    private Integer yesterdayActivityApply;//昨日活动报名数

    private Integer yesterdayVip;//昨日新注册会员

    private String yesterdayForNotAudit;//申请加V未审核

    private Integer totalUser;//总用户

    public String getYesterdayForNotAudit() {
        return yesterdayForNotAudit;
    }

    public void setYesterdayForNotAudit(String yesterdayForNotAudit) {
        this.yesterdayForNotAudit = yesterdayForNotAudit;
    }

    public Integer getTotalUser() {
        return totalUser;
    }

    public void setTotalUser(Integer totalUser) {
        this.totalUser = totalUser;
    }

    public Integer getYesterdayPost() {
        return yesterdayPost;
    }

    public void setYesterdayPost(Integer yesterdayPost) {
        this.yesterdayPost = yesterdayPost;
    }

    public Integer getYesterdayOrders() {
        return yesterdayOrders;
    }

    public void setYesterdayOrders(Integer yesterdayOrders) {
        this.yesterdayOrders = yesterdayOrders;
    }

    public Integer getYesterdayActivityApply() {
        return yesterdayActivityApply;
    }

    public void setYesterdayActivityApply(Integer yesterdayActivityApply) {
        this.yesterdayActivityApply = yesterdayActivityApply;
    }

    public Integer getYesterdayVip() {
        return yesterdayVip;
    }

    public void setYesterdayVip(Integer yesterdayVip) {
        this.yesterdayVip = yesterdayVip;
    }

    public String getNewPost() {
        return newPost;
    }

    public void setNewPost(String newPost) {
        this.newPost = newPost;
    }

    public String getNewMember() {
        return newMember;
    }

    public void setNewMember(String newMember) {
        this.newMember = newMember;
    }

    public String getNewActivityApply() {
        return newActivityApply;
    }

    public void setNewActivityApply(String newActivityApply) {
        this.newActivityApply = newActivityApply;
    }

    public String getNewOrderForm() {
        return newOrderForm;
    }

    public void setNewOrderForm(String newOrderForm) {
        this.newOrderForm = newOrderForm;
    }

    public String getApplyForNotAudit() {
        return applyForNotAudit;
    }

    public void setApplyForNotAudit(String applyForNotAudit) {
        this.applyForNotAudit = applyForNotAudit;
    }
}
