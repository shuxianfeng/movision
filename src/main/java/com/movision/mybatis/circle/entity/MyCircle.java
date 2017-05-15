package com.movision.mybatis.circle.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/5/15 10:22
 */
public class MyCircle implements Serializable {
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

    private Integer status;

    private Integer supportnum;

    private Integer isdiscover;

    private Integer orderid;

    private Integer isdel;

    private Integer userid;

    private Integer postnewnum;

    public void setPostnewnum(Integer postnewnum) {
        this.postnewnum = postnewnum;
    }

    public Integer getPostnewnum() {

        return postnewnum;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIsofficial(Integer isofficial) {
        this.isofficial = isofficial;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public void setMaylikeimg(String maylikeimg) {
        this.maylikeimg = maylikeimg;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setSupportnum(Integer supportnum) {
        this.supportnum = supportnum;
    }

    public void setIsdiscover(Integer isdiscover) {
        this.isdiscover = isdiscover;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getId() {

        return id;
    }

    public Integer getIsofficial() {
        return isofficial;
    }

    public Integer getScope() {
        return scope;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getNotice() {
        return notice;
    }

    public Integer getPermission() {
        return permission;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public String getMaylikeimg() {
        return maylikeimg;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getSupportnum() {
        return supportnum;
    }

    public Integer getIsdiscover() {
        return isdiscover;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public Integer getUserid() {
        return userid;
    }
}
