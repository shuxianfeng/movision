package com.movision.mybatis.circle.entity;

import com.movision.mybatis.user.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用于返回圈子首页列表
 *
 * @Author zhurui
 * @Date 2017/2/16 11:32
 */
public class CircleIndexList implements Serializable {
    private Integer category;//圈子分类
    private String categoryname;//圈子分类名称
    private List<User> circlemaster;//圈主
    private List<User> circlemanagerlist;//圈子管理员列表
    private Integer follownum;//圈子关注数
    private Integer follownewnum;//圈子今日关注数
    private Integer postnum;//圈子帖子数
    private Integer postnewnum;//圈子今日更新帖子数
    private Integer isessencenum;//圈子中精贴数
    private Integer supportnum;//支持数
    private Date intime;//时间
    private List<CircleVo> classify;//分类列表

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public List<CircleVo> getClassify() {
        return classify;
    }

    public void setClassify(List<CircleVo> classify) {
        this.classify = classify;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<User> getCirclemaster() {
        return circlemaster;
    }

    public void setCirclemaster(List<User> circlemaster) {
        this.circlemaster = circlemaster;
    }

    public List<User> getCirclemanagerlist() {
        return circlemanagerlist;
    }

    public void setCirclemanagerlist(List<User> circlemanagerlist) {
        this.circlemanagerlist = circlemanagerlist;
    }

    public Integer getFollownum() {
        return follownum;
    }

    public void setFollownum(Integer follownum) {
        this.follownum = follownum;
    }

    public Integer getFollownewnum() {
        return follownewnum;
    }

    public void setFollownewnum(Integer follownewnum) {
        this.follownewnum = follownewnum;
    }

    public Integer getPostnum() {
        return postnum;
    }

    public void setPostnum(Integer postnum) {
        this.postnum = postnum;
    }

    public Integer getPostnewnum() {
        return postnewnum;
    }

    public void setPostnewnum(Integer postnewnum) {
        this.postnewnum = postnewnum;
    }

    public Integer getIsessencenum() {
        return isessencenum;
    }

    public void setIsessencenum(Integer isessencenum) {
        this.isessencenum = isessencenum;
    }

    public Integer getSupportnum() {
        return supportnum;
    }

    public void setSupportnum(Integer supportnum) {
        this.supportnum = supportnum;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}
