package com.movision.mybatis.goods.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 10:15
 */
public class GoodsSearchEntity implements Serializable {
    private Integer id;

    private String name;

    private String subname;

    private Double price;

    private Double origprice;

    private String attribute;

    private Integer protype;

    private String protype_name;

    private String brandid;

    private String brand_CNName;


    private Integer sales;

    private Date onlinetime;

    /**
     * 商品列表展示的图片
     */
    private String url;

    public void setProtype_name(String protype_name) {
        this.protype_name = protype_name;
    }

    public String getProtype_name() {

        return protype_name;
    }

    @Override
    public String toString() {
        return "GoodsSearchEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", price=" + price +
                ", origprice=" + origprice +
                ", attribute='" + attribute + '\'' +
                ", protype=" + protype +
                ", protype_name=" + protype_name +
                ", brandid='" + brandid + '\'' +
                ", brand_CNName='" + brand_CNName + '\'' +
                ", sales=" + sales +
                ", onlinetime=" + onlinetime +
                ", url='" + url + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setOrigprice(Double origprice) {
        this.origprice = origprice;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public void setProtype(Integer protype) {
        this.protype = protype;
    }


    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public void setBrand_CNName(String brand_CNName) {
        this.brand_CNName = brand_CNName;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public void setOnlinetime(Date onlinetime) {
        this.onlinetime = onlinetime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubname() {
        return subname;
    }

    public Double getPrice() {
        return price;
    }

    public Double getOrigprice() {
        return origprice;
    }

    public String getAttribute() {
        return attribute;
    }

    public Integer getProtype() {
        return protype;
    }


    public String getBrandid() {
        return brandid;
    }

    public String getBrand_CNName() {
        return brand_CNName;
    }

    public Integer getSales() {
        return sales;
    }

    public Date getOnlinetime() {
        return onlinetime;
    }

    public String getUrl() {
        return url;
    }
}
