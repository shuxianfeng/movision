package com.movision.mybatis.combo.entity;

import java.io.Serializable;
import java.util.Date;

public class Combo implements Serializable {
    private Integer id;

    private Integer comboid;

    private String comboname;

    private Double combodiscountprice;//套餐折后总价

    private Date intime;//套餐添加时间

    private String imgurl;

    private String width;

    private String height;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getComboid() {
        return comboid;
    }

    public void setComboid(Integer comboid) {
        this.comboid = comboid;
    }

    public String getComboname() {
        return comboname;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname == null ? null : comboname.trim();
    }

    public Double getCombodiscountprice() {
        return combodiscountprice;
    }

    public void setCombodiscountprice(Double combodiscountprice) {
        this.combodiscountprice = combodiscountprice;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}