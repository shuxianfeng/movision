package com.movision.mybatis.circle.entity;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.user.entity.User;

import java.util.Date;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/21 19:09
 */
public class CircleDetails {
    private Integer id;

    private Integer isofficial;//是否为官方

    private Integer scope;//开放范围

    private String phone;//圈主手机号

    private String photo;//banner图

    private Integer category;//所属分类id

    private Integer code;//

    private String name;//圈子名称

    private String introduction;//圈子简介

    private String notice;//圈子公共

    private Integer permission;//圈子权限

    private Integer isrecommend;//是否被推荐的圈子

    private String maylikeimg;//首页小方图

    private Date createtime;//创建时间

    private Integer status;//圈子状态

    private Integer supportnum;//支持数量

    private Integer isdiscover;//是否为首页内容

    private Integer orderid;//排序

    private List<Integer> orderids;//排序列表

    private Integer isdel;//是否逻辑删除

    private Integer circlemanid;//圈主

    private String nickname;//圈主名

    private String categoryname;//圈子所属类型

    private List<User> admin;//管理员列表

    private String userid;//创建人id

    private String username;//创建人名称

    private String erweima;//二维码

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsofficial() {
        return isofficial;
    }

    public void setIsofficial(Integer isofficial) {
        this.isofficial = isofficial;
    }

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public String getMaylikeimg() {
        return maylikeimg;
    }

    public void setMaylikeimg(String maylikeimg) {
        this.maylikeimg = maylikeimg;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSupportnum() {
        return supportnum;
    }

    public void setSupportnum(Integer supportnum) {
        this.supportnum = supportnum;
    }

    public Integer getIsdiscover() {
        return isdiscover;
    }

    public void setIsdiscover(Integer isdiscover) {
        this.isdiscover = isdiscover;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public List<Integer> getOrderids() {
        return orderids;
    }

    public void setOrderids(List<Integer> orderids) {
        this.orderids = orderids;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getCirclemanid() {
        return circlemanid;
    }

    public void setCirclemanid(Integer circlemanid) {
        this.circlemanid = circlemanid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<User> getAdmin() {
        return admin;
    }

    public void setAdmin(List<User> admin) {
        this.admin = admin;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErweima() {
        return erweima;
    }

    public void setErweima(String erweima) {
        this.erweima = erweima;
    }
}
