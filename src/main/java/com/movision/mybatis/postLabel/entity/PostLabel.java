package com.movision.mybatis.postLabel.entity;

import java.util.Date;

public class PostLabel {
    private Integer id;

    private String name;

    private Integer heatValue;

    private Integer userid;

    private Date intime;

    private Integer type;

    private String photo;

    private Integer isdel;

    private String citycode;

    private Integer isrecommend;

    private Integer useCount;

    private Integer fans;

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getUseCount() {

        return useCount;
    }

    public Integer getFans() {
        return fans;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCitycode() {

        return citycode;
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

    /**
     * 重写equals方法，用于比对
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof PostLabel && this.id != null && this.id.equals(((PostLabel) obj).getId());
    }

    @Override
    public String toString() {
        return "PostLabel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", heatValue=" + heatValue +
                ", userid=" + userid +
                ", intime=" + intime +
                ", type=" + type +
                ", photo='" + photo + '\'' +
                ", isdel=" + isdel +
                ", citycode='" + citycode + '\'' +
                ", isrecommend=" + isrecommend +
                ", useCount=" + useCount +
                ", fans=" + fans +
                '}';
    }
}