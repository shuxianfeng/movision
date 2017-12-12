package com.movision.mybatis.circle.entity;

import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.user.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/19 11:41
 */
public class CircleList implements Serializable {
    private Integer id;

    private Integer category;//圈子分类

    private String name;

    private List<String> circlemaster;//圈主

    private String circlename;//圈主名称

    private Integer circleUserid;//圈主id

    private List<String> circlemanagerlist;//圈子管理员列表

    private Integer heatvalue;//热度

    private Integer follownum;//圈子关注数

    private Integer follownewnum;//圈子今日关注数

    private Integer postnum;//圈子帖子数

    private Integer postnewnum;//今日新增帖子数量

    private Integer isessencenum;//圈子中精贴数

    private Integer supportnum;//支持数

    private Integer isdiscover;//推荐

    private Integer isrecommend;//首页推荐

    private Integer status;//审核状态0 待审核 1 审核通过 2 审核不通过

    private Date intime;//时间

    private List<CircleList> foldCircle;//折叠的圈子

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCirclemaster() {
        return circlemaster;
    }

    public void setCirclemaster(List<String> circlemaster) {
        this.circlemaster = circlemaster;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public Integer getCircleUserid() {
        return circleUserid;
    }

    public void setCircleUserid(Integer circleUserid) {
        this.circleUserid = circleUserid;
    }

    public List<String> getCirclemanagerlist() {
        return circlemanagerlist;
    }

    public void setCirclemanagerlist(List<String> circlemanagerlist) {
        this.circlemanagerlist = circlemanagerlist;
    }

    public Integer getHeatvalue() {
        return heatvalue;
    }

    public void setHeatvalue(Integer heatvalue) {
        this.heatvalue = heatvalue;
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

    public Integer getIsdiscover() {
        return isdiscover;
    }

    public void setIsdiscover(Integer isdiscover) {
        this.isdiscover = isdiscover;
    }

    public Integer getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(Integer isrecommend) {
        this.isrecommend = isrecommend;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public List<CircleList> getFoldCircle() {
        return foldCircle;
    }

    public void setFoldCircle(List<CircleList> foldCircle) {
        this.foldCircle = foldCircle;
    }
}
