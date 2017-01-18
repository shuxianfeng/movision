package com.movision.mybatis.circle.entity;

import java.util.Date;

public class Circle {
    private Integer id;

    private Integer isofficial;

    private Integer scope;

    private String phone;

    private String photo;

    private Integer category;

    private Integer code;

    private String name;

    private String introduction;

    private String notice;

    private Integer permission;

    private Integer isrecommend;

    private String maylikeimg;

    private Date createtime;

    private Integer supportnum;

    private Integer isdiscover;

    private Integer orderid;

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
        this.phone = phone == null ? null : phone.trim();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo == null ? null : photo.trim();
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
        this.name = name == null ? null : name.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

    public String getMaylikeimg() {
        return maylikeimg;
    }

    public void setMaylikeimg(String maylikeimg) {
        this.maylikeimg = maylikeimg;
    }
}