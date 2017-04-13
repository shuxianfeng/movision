package com.movision.mybatis.goodsAssessmentImg.entity;

import java.io.Serializable;

public class GoodsAssessmentImg implements Serializable {
    private Integer id;

    private Integer assessmentid;

    private String imgurl;

    private Integer orderid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssessmentid() {
        return assessmentid;
    }

    public void setAssessmentid(Integer assessmentid) {
        this.assessmentid = assessmentid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
}