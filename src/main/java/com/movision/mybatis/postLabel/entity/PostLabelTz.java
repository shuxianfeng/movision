package com.movision.mybatis.postLabel.entity;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/7/24 18:28
 */
public class PostLabelTz {

    private Integer id;

    private String name;

    private Integer heatValue;

    private Integer userid;

    private Date intime;

    private Integer type;

    private String photo;
    private Integer count;

    private Integer follow;
    private Integer isfollow;//该用户又没有关注该标签

    public Integer getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(Integer isfollow) {
        this.isfollow = isfollow;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getType() {

        return type;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getHeatValue() {
        return heatValue;
    }

    public void setHeatValue(Integer heatValue) {
        this.heatValue = heatValue;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}
