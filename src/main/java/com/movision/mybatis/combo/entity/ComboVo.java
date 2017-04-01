package com.movision.mybatis.combo.entity;

import java.util.Date;

/**
 * @Author shuxf
 * @Date 2017/2/20 19:01
 */
public class ComboVo {

    private Integer id;

    private Integer comboid;

    private String comboname;

    private Double combodiscountprice;//套餐折后总价

    private Date intime;//套餐添加时间

    private String imgurl;//商品小方图

    private String width;

    private String height;

    private Integer stock;//套餐剩余库存

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
        this.comboname = comboname;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
