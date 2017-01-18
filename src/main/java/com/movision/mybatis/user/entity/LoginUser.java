package com.movision.mybatis.user.entity;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/18 16:26
 */
public class LoginUser {

    private Integer id;

    private String openid;

    private String phone;

    private String uuid;

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

    private String role;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
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
        return level;
    }

    public Integer getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    public LoginUser(Integer id, String openid, String phone, String uuid, String invitecode, String referrals, String nickname, String sign, Integer attention, Integer fans, Integer points, String photo, Integer sex, Date birthday, String province, String city, Date intime, Integer growth, Integer level, Integer status, String role) {
        this.id = id;
        this.openid = openid;
        this.phone = phone;
        this.uuid = uuid;
        this.invitecode = invitecode;
        this.referrals = referrals;
        this.nickname = nickname;
        this.sign = sign;
        this.attention = attention;
        this.fans = fans;
        this.points = points;
        this.photo = photo;
        this.sex = sex;
        this.birthday = birthday;
        this.province = province;
        this.city = city;
        this.intime = intime;
        this.growth = growth;
        this.level = level;
        this.status = status;
        this.role = role;
    }


}
