package com.movision.mybatis.postLabel.entity;

import java.util.Date;

public class PostLabelDetails {
    private Integer id;

    private String name;

    private Integer heatValue;

    private Integer userid;

    private Date intime;

    private Integer type;

    private String photo;

    private String userName;

    private Integer isdel;

    private Integer useQuantity;

    private Integer isrecommend;

    private String citycode;

    private String cityName;

    private Integer useCount;

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(Integer useQuantity) {
        this.useQuantity = useQuantity;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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