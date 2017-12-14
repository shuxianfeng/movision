package com.movision.mybatis.user.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private Integer id;

    private String openid;

    private String qq;

    private String sina;

    private String email;

    private String phone;

    private Integer orderid;

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

    private Integer level;//2.0后改为用户等级

    private Integer isdv;//是否为大V：0 否 1 是

    private Integer status;

    private Date applydate;

    private String deviceno;

    private Date loginTime;

    private Integer isrecommend;

    private Integer heat_value;

    private String longitude;

    private String latitude;

    private String ip;

    private String ip_city;

    private Integer source;

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getSource() {

        return source;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getIsrecommend() {

        return isrecommend;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setIp_city(String ip_city) {
        this.ip_city = ip_city;
    }

    public String getLongitude() {

        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getIp() {
        return ip;
    }

    public String getIp_city() {
        return ip_city;
    }

    public Integer getHeat_value() {
        return heat_value;
    }

    public void setHeat_value(Integer heat_value) {
        this.heat_value = heat_value;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() {

        return loginTime;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getDeviceno() {

        return deviceno;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode == null ? null : invitecode.trim();
    }

    public String getReferrals() {
        return referrals;
    }

    public void setReferrals(String referrals) {
        this.referrals = referrals == null ? null : referrals.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }

    public Integer getAttention() {
        return attention;
    }

    public void setAttention(Integer attention) {
        this.attention = attention;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getGrowth() {
        return growth;
    }

    public void setGrowth(Integer growth) {
        this.growth = growth;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getIsdv() {
        return isdv;
    }

    public void setIsdv(Integer isdv) {
        this.isdv = isdv;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", qq='" + qq + '\'' +
                ", sina='" + sina + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", orderid=" + orderid +
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
                ", isdv=" + isdv +
                ", status=" + status +
                ", applydate=" + applydate +
                ", deviceno='" + deviceno + '\'' +
                ", loginTime=" + loginTime +
                ", isrecommend=" + isrecommend +
                ", heat_value=" + heat_value +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", ip='" + ip + '\'' +
                ", ip_city='" + ip_city + '\'' +
                ", source=" + source +
                '}';
    }
}