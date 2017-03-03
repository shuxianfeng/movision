package com.movision.mybatis.goodscombo.entity;

import java.util.Date;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/3/3 10:04
 */
public class GoodsComboVo {
    private Integer id;

    private Integer comboid;

    private String comboname;
    private Double combodiscountprice;
    private Integer sales;
    private Date intime;

    public String getComboname() {
        return comboname;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname;
    }

    public Double getCombodiscountprice() {
        return combodiscountprice;
    }

    public void setCombodiscountprice(Double combodiscountprice) {
        this.combodiscountprice = combodiscountprice;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    private Integer goodsid;

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

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    private String name;

    private Double price;

    private Double sumprice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSumprice() {
        return sumprice;
    }

    public void setSumprice(Double sumprice) {
        this.sumprice = sumprice;
    }

    public List<GoodsComboVo> getList() {
        return list;
    }

    public void setList(List<GoodsComboVo> list) {
        this.list = list;
    }

    private List<GoodsComboVo> list;
}
