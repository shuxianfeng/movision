package com.movision.mybatis.circle.entity;

import com.movision.mybatis.post.entity.Post;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/19 11:41
 */
public class CircleVo {
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

    private List<Post> hotPostList;

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

    public List<Post> getHotPostList() {
        return hotPostList;
    }

    public void setHotPostList(List<Post> hotPostList) {
        this.hotPostList = hotPostList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
