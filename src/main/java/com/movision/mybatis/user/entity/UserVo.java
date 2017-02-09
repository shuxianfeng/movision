package com.movision.mybatis.user.entity;

import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/2/6 16:54
 */
public class UserVo {
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

    private Integer livesum;//直播个数

    private Integer becollectsum;//被收藏次数

    private Integer besharesum;//被分享次数

    private Integer followsum;//关注别人的个数（关注数）

    private Integer befollowsum;//被关注的次数（粉丝数）

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

    public Integer getLivesum() {
        return livesum;
    }

    public void setLivesum(Integer livesum) {
        this.livesum = livesum;
    }

    public Integer getBecollectsum() {
        return becollectsum;
    }

    public void setBecollectsum(Integer becollectsum) {
        this.becollectsum = becollectsum;
    }

    public Integer getBesharesum() {
        return besharesum;
    }

    public void setBesharesum(Integer besharesum) {
        this.besharesum = besharesum;
    }

    public Integer getFollowsum() {
        return followsum;
    }

    public void setFollowsum(Integer followsum) {
        this.followsum = followsum;
    }

    public Integer getBefollowsum() {
        return befollowsum;
    }

    public void setBefollowsum(Integer befollowsum) {
        this.befollowsum = befollowsum;
    }
}
