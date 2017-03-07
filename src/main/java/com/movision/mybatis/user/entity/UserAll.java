package com.movision.mybatis.user.entity;

import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/2/6 16:54
 */
public class UserAll {
    private Integer id;

    private String phone;//手机号

    private String nickname;//用户名

    private Integer fans;//被关注的次数（粉丝数）

    private Integer points;//用户积分

    private String coupon;//优惠券

    private Date intime;//注册时间

    private Integer level;//vip

    private Integer status;//状态1 封号

    private Integer postsum;//帖子数量

    private Integer isessencesum;//精贴数量

    private Date applydate;//申请时间

    private Integer authstatus;//实名认证

    private String sina;//登录微博？

    private String qq;//QQ登录

    private String openid;//微信登录

    private String login;//用户登录方式1:qq 2:微信 3: 微博 4：QQ、微信 5：QQ、微博 6：微信、微博 7：QQ、微信、微博

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getAuthstatus() {
        return authstatus;
    }

    public void setAuthstatus(Integer authstatus) {
        this.authstatus = authstatus;
    }

    public String getSina() {
        return sina;
    }

    public void setSina(String sina) {
        this.sina = sina;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPostsum() {
        return postsum;
    }

    public void setPostsum(Integer postsum) {
        this.postsum = postsum;
    }

    public Integer getIsessencesum() {
        return isessencesum;
    }

    public void setIsessencesum(Integer isessencesum) {
        this.isessencesum = isessencesum;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }
}
