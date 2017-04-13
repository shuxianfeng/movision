package com.movision.mybatis.category.entity;

import com.movision.mybatis.bossUser.entity.BossUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/22 19:20
 */
public class CategoryVo implements Serializable {
    private Integer id;

    private String categoryname;

    private Date intime;

    private String discoverpageurl;

    private List<CircleAndCircle> circleAndCircles;//圈子名称和圈主

    private List<BossUser> Administrator;//圈子管理员

    public List<BossUser> getAdministrator() {
        return Administrator;
    }

    public void setAdministrator(List<BossUser> administrator) {
        Administrator = administrator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public String getDiscoverpageurl() {
        return discoverpageurl;
    }

    public void setDiscoverpageurl(String discoverpageurl) {
        this.discoverpageurl = discoverpageurl;
    }

    public List<CircleAndCircle> getCircleAndCircles() {
        return circleAndCircles;
    }

    public void setCircleAndCircles(List<CircleAndCircle> circleAndCircles) {
        this.circleAndCircles = circleAndCircles;
    }
}
