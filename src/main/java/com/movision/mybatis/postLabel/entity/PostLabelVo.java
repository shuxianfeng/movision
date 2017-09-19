package com.movision.mybatis.postLabel.entity;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/7/19 19:52
 */
public class PostLabelVo {
    private Integer userid;

    private Date intime;

    private Integer type;

    private String photo;

    private Integer isdel;

    private String citycode;

    private Integer isrecommend;

    private Integer useCount;

    private Integer fans;

    private Integer isfollow;//当前登录用户有没有关注过该标签：0 未关注 1 已关注

    private Integer circleid;
    private Integer labelid;
    private String name;
    private Integer heatvalue;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public Integer getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(Integer isfollow) {
        this.isfollow = isfollow;
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeatvalue() {
        return heatvalue;
    }

    public void setHeatvalue(Integer heatvalue) {
        this.heatvalue = heatvalue;
    }
}
