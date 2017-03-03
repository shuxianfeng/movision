package com.movision.mybatis.user.entity;

import com.movision.mybatis.province.entity.ProvinceVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserParticulars implements Serializable {

    private Integer id;

    private String openid;//微信

    private String qq;

    private String sina;//微博

    private String phone;//手机号

    private String nickname;//昵称

    private String photo;//头像

    private Integer sex;//性别

    private Date birthday;//生日

    private String province;//省

    private String city;//市

    private Date intime;//注册时间

    private Integer level;//vip

    private Integer grade;//vip等级

    private String viptime;//成为VIP时间

    private String status;//是否封号

    private List<ProvinceVo> provinces;//地址

    private String postnum;//帖子数量

    private String isessenceid;//精贴数量

    private String reward;//打赏数量

    private String byreward;//被打赏

    private String collectpost;//收藏帖子数量

    private String collectgoods;//收藏商品数量

    private String bycollect;//被收藏数量

    private String sharegoods;//分享商品数量

    private String sharepost;//分享帖子数量

    private String byshare;//被分享数量

    private String commentpost;//帖子评论数量

    private String commentassessment;//评论商品数量

    private String bycomment;//被评论数量

    private String zansum;//点赞次数

    private String byzansum;//被点赞次数

    private String circlefollow;//圈子关注数量

    private String ishot;//发现推荐

    private Date oneissuepost;//第一次发帖时间

    private Date onecollection;//第一次收藏

    public Date getOnecollection() {
        return onecollection;
    }

    public void setOnecollection(Date onecollection) {
        this.onecollection = onecollection;
    }

    public Date getOneissuepost() {
        return oneissuepost;
    }

    public void setOneissuepost(Date oneissuepost) {
        this.oneissuepost = oneissuepost;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getViptime() {
        return viptime;
    }

    public void setViptime(String viptime) {
        this.viptime = viptime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProvinceVo> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceVo> provinces) {
        this.provinces = provinces;
    }

    public String getPostnum() {
        return postnum;
    }

    public void setPostnum(String postnum) {
        this.postnum = postnum;
    }

    public String getIsessenceid() {
        return isessenceid;
    }

    public void setIsessenceid(String isessenceid) {
        this.isessenceid = isessenceid;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getByreward() {
        return byreward;
    }

    public void setByreward(String byreward) {
        this.byreward = byreward;
    }

    public String getCollectpost() {
        return collectpost;
    }

    public void setCollectpost(String collectpost) {
        this.collectpost = collectpost;
    }

    public String getCollectgoods() {
        return collectgoods;
    }

    public void setCollectgoods(String collectgoods) {
        this.collectgoods = collectgoods;
    }

    public String getBycollect() {
        return bycollect;
    }

    public void setBycollect(String bycollect) {
        this.bycollect = bycollect;
    }

    public String getSharegoods() {
        return sharegoods;
    }

    public void setSharegoods(String sharegoods) {
        this.sharegoods = sharegoods;
    }

    public String getSharepost() {
        return sharepost;
    }

    public void setSharepost(String sharepost) {
        this.sharepost = sharepost;
    }

    public String getByshare() {
        return byshare;
    }

    public void setByshare(String byshare) {
        this.byshare = byshare;
    }

    public String getCommentpost() {
        return commentpost;
    }

    public void setCommentpost(String commentpost) {
        this.commentpost = commentpost;
    }

    public String getCommentassessment() {
        return commentassessment;
    }

    public void setCommentassessment(String commentassessment) {
        this.commentassessment = commentassessment;
    }

    public String getBycomment() {
        return bycomment;
    }

    public void setBycomment(String bycomment) {
        this.bycomment = bycomment;
    }

    public String getZansum() {
        return zansum;
    }

    public void setZansum(String zansum) {
        this.zansum = zansum;
    }

    public String getByzansum() {
        return byzansum;
    }

    public void setByzansum(String byzansum) {
        this.byzansum = byzansum;
    }

    public String getCirclefollow() {
        return circlefollow;
    }

    public void setCirclefollow(String circlefollow) {
        this.circlefollow = circlefollow;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }
}