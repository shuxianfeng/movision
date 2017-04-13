package com.movision.mybatis.circleCategory.entity;

import java.io.Serializable;
import java.util.Date;

public class CircleCategory implements Serializable {
    private Integer id;

    private String categoryname;

    private Date intime;

    private String discoverpageurl;

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
        this.categoryname = categoryname == null ? null : categoryname.trim();
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
        this.discoverpageurl = discoverpageurl == null ? null : discoverpageurl.trim();
    }
}