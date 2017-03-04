package com.movision.mybatis.goodscombo.entity;

import java.util.Date;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/3/3 10:04
 */
public class GoodsComboVo {
    private Integer id;


    private String comboname;
    private Double combodiscountprice;
    private Integer sales;
    private Date intime;
    private Integer comboid;
    private Double sum;


    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    private List<GoodsComboVo> list;
    private String name;

    private Double price;

    public List<GoodsComboVo> getList() {
        return list;
    }

    public Double getSumprice() {
        return sumprice;
    }

    public void setSumprice(Double sumprice) {
        this.sumprice = sumprice;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<GoodsComboVo> list) {
        this.list = list;
    }

    private Double sumprice;
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





}
