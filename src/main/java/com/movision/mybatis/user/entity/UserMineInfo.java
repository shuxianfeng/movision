package com.movision.mybatis.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/7/24 15:33
 * 美番2.0--我的板块上半部分专属实体
 */
public class UserMineInfo implements Serializable {

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

    private Integer attention;//关注数

    private Integer fans;//粉丝数

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

    private String deviceno;

    private Date loginTime;

    private Integer isrecommend;

    private Integer heat_value;

    private String longitude;

    private String latitude;

    private String ip;

    private String ip_city;

    private Integer bezansum;//用户发布的帖子被赞总数

    private Integer becollectsum;//用户发布的帖子被收藏总数

    private Integer relasePostSum;//用户发布的帖子总数

    private Integer partActiveSum;//用户参加的活动总数

    private Integer collectionsum;//用户收藏的帖子和活动总数

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getReferrals() {
        return referrals;
    }

    public void setReferrals(String referrals) {
        this.referrals = referrals;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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
        this.photo = photo;
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
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getApplydate() {
        return applydate;
    }

    public void setApplydate(Date applydate) {
        this.applydate = applydate;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getHeat_value() {
        return heat_value;
    }

    public void setHeat_value(Integer heat_value) {
        this.heat_value = heat_value;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp_city() {
        return ip_city;
    }

    public void setIp_city(String ip_city) {
        this.ip_city = ip_city;
    }

    public Integer getBezansum() {
        return bezansum;
    }

    public void setBezansum(Integer bezansum) {
        this.bezansum = bezansum;
    }

    public Integer getBecollectsum() {
        return becollectsum;
    }

    public void setBecollectsum(Integer becollectsum) {
        this.becollectsum = becollectsum;
    }

    public Integer getRelasePostSum() {
        return relasePostSum;
    }

    public void setRelasePostSum(Integer relasePostSum) {
        this.relasePostSum = relasePostSum;
    }

    public Integer getPartActiveSum() {
        return partActiveSum;
    }

    public void setPartActiveSum(Integer partActiveSum) {
        this.partActiveSum = partActiveSum;
    }

    public Integer getCollectionsum() {
        return collectionsum;
    }

    public void setCollectionsum(Integer collectionsum) {
        this.collectionsum = collectionsum;
    }
}
