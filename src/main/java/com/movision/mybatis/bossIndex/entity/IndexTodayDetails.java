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

    private String theDayBeforeVip;//前天新注册会员

    private String theDayBeforePost;//前天帖子

    private String theDayBeforeActivityApply;//前天活动报名

    private String theDayBeforeForNotAudit;//前天申请vip未审核

    private String twoDaysEarlierVip;//两天前注册会员

    private String twoDaysEarlierPost;//两天前帖子

    private String twoDaysEarlierActivityApply;//两天前活动报名

    private String twoDaysEarlierForNotAudit;//两天前申请vip未审核

    public String getTheDayBeforeVip() {
        return theDayBeforeVip;
    }

    public void setTheDayBeforeVip(String theDayBeforeVip) {
        this.theDayBeforeVip = theDayBeforeVip;
    }

    public String getTheDayBeforePost() {
        return theDayBeforePost;
    }

    public void setTheDayBeforePost(String theDayBeforePost) {
        this.theDayBeforePost = theDayBeforePost;
    }

    public String getTheDayBeforeActivityApply() {
        return theDayBeforeActivityApply;
    }

    public void setTheDayBeforeActivityApply(String theDayBeforeActivityApply) {
        this.theDayBeforeActivityApply = theDayBeforeActivityApply;
    }

    public String getTheDayBeforeForNotAudit() {
        return theDayBeforeForNotAudit;
    }

    public void setTheDayBeforeForNotAudit(String theDayBeforeForNotAudit) {
        this.theDayBeforeForNotAudit = theDayBeforeForNotAudit;
    }

    public String getTwoDaysEarlierVip() {
        return twoDaysEarlierVip;
    }

    public void setTwoDaysEarlierVip(String twoDaysEarlierVip) {
        this.twoDaysEarlierVip = twoDaysEarlierVip;
    }

    public String getTwoDaysEarlierPost() {
        return twoDaysEarlierPost;
    }

    public void setTwoDaysEarlierPost(String twoDaysEarlierPost) {
        this.twoDaysEarlierPost = twoDaysEarlierPost;
    }

    public String getTwoDaysEarlierActivityApply() {
        return twoDaysEarlierActivityApply;
    }

    public void setTwoDaysEarlierActivityApply(String twoDaysEarlierActivityApply) {
        this.twoDaysEarlierActivityApply = twoDaysEarlierActivityApply;
    }

    public String getTwoDaysEarlierForNotAudit() {
        return twoDaysEarlierForNotAudit;
    }

    public void setTwoDaysEarlierForNotAudit(String twoDaysEarlierForNotAudit) {
        this.twoDaysEarlierForNotAudit = twoDaysEarlierForNotAudit;
    }

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
