package com.movision.mybatis.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于shiroRealm，包含登录app的用户的所有信息
 * 比User多出  role, accid, imtoken
 *
 * @Author zhuangyuhao
 * @Date 2017/1/18 16:26
 */
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 3748849614356147652L;

    private Integer id;

    private String openid;

    private String qq;

    private String sina;

    private String email;

    private String phone;

    private String token;

    private String invitecode;

    private String referrals;

    private String nickname;

    private String sign;

    private Integer attention;

    private Integer fans;

    private Integer points;

    private String photo;

    private Integer sex;

    private Date birthday;

    private String province;

    private String city;

    private Date intime;

    private Integer growth;

    private Integer level;

    private Integer status;

    private Date applydate;

    private String role;    //角色

    private String accid;

    private String imtoken;

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public void setImtoken(String imtoken) {
        this.imtoken = imtoken;
    }

    public Date getApplydate() {

        return applydate;
    }

    public String getAccid() {
        return accid;
    }

    public String getImtoken() {
        return imtoken;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSina() {
        return sina;
    }

    public void setSina(String sina) {
        this.sina = sina;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public void setReferrals(String referrals) {
        this.referrals = referrals;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setAttention(Integer attention) {
        this.attention = attention;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getOpenid() {
        return openid;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public String getReferrals() {
        return referrals;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSign() {
        return sign;
    }

    public Integer getAttention() {
        return attention;
    }

    public Integer getFans() {
        return fans;
    }

    public Integer getPoints() {
        return points;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public Date getIntime() {
        return intime;
    }

    public Integer getGrowth() {
        return growth;
    }

    public Integer getLevel() {
        return level == null ? 0 : level;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "LoginUser{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", qq='" + qq + '\'' +
                ", sina='" + sina + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", invitecode='" + invitecode + '\'' +
                ", referrals='" + referrals + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sign='" + sign + '\'' +
                ", attention=" + attention +
                ", fans=" + fans +
                ", points=" + points +
                ", photo='" + photo + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", intime=" + intime +
                ", growth=" + growth +
                ", level=" + level +
                ", status=" + status +
                ", applydate=" + applydate +
                ", role='" + role + '\'' +
                ", accid='" + accid + '\'' +
                ", imtoken='" + imtoken + '\'' +
                '}';
    }
}
