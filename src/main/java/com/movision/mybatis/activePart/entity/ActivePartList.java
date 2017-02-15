package com.movision.mybatis.activePart.entity;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/13 16:10
 */
public class ActivePartList {

    private Double moneypay;//实付金额
    private Double moneyying;//应付金额
    private Integer payStatue;//付款方式
    private  String phone;//联系方式
    private Integer userid;//用户id
    private Date begintime;
    private Date endtime;
    private Integer postid;//帖子id



    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }



    public Double getActivefee() {
        return activefee;
    }

    public void setActivefee(Double activefee) {
        this.activefee = activefee;
    }

    private Double activefee;

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getBegintime() {

        return begintime;
    }

    public void setBegintime(Date begintime) {
        this.begintime = begintime;
    }

    public String getActivestatue() {
        return activestatue;
    }

    public void setActivestatue(String activestatue) {
        this.activestatue = activestatue;
    }

    public Date getIntime() {

        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserid() {

        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPayStatue() {

        return payStatue;
    }

    public void setPayStatue(Integer payStatue) {
        this.payStatue = payStatue;
    }

    public Double getMoneyying() {

        return moneyying;
    }

    public void setMoneyying(Double moneyying) {
        this.moneyying = moneyying;
    }

    public Double getMoneypay() {

        return moneypay;
    }

    public void setMoneypay(Double moneypay) {
        this.moneypay = moneypay;
    }

    private String nickname;//发帖人
    private Date intime;//参与时间
    private  String activestatue;//活动状态
}
