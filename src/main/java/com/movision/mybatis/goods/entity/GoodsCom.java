package com.movision.mybatis.goods.entity;

/**
 * @Author zhanglei
 * @Date 2017/3/4 17:48
 */
public class GoodsCom {


    public Integer getComboid() {
        return comboid;
    }

    public void setComboid(Integer comboid) {
        this.comboid = comboid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getOrigprice() {
        return origprice;
    }

    public void setOrigprice(Double origprice) {
        this.origprice = origprice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    private Integer comboid;
    private Integer goodsid;
    private String name;
    private Double origprice;
    private Integer stock;
    private Integer sales;
    private String attribute;
    private Integer id;
    private String typename;
    private String brandname;
}
