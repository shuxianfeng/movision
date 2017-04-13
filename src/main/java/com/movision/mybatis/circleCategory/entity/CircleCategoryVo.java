package com.movision.mybatis.circleCategory.entity;

import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/21 14:43
 */
public class CircleCategoryVo implements Serializable {
    private Integer id;

    private String categoryname;

    private Date intime;

    private String discoverpageurl;

    private List<CircleVo> circleList;

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

    public List<CircleVo> getCircleList() {
        return circleList;
    }

    public void setCircleList(List<CircleVo> circleList) {
        this.circleList = circleList;
    }
}
