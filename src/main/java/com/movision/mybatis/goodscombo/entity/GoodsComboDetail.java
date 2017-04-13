package com.movision.mybatis.goodscombo.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/3/3 15:55
 */
public class GoodsComboDetail implements Serializable {

    private Integer comboid;

    public Integer getComboid() {
        return comboid;
    }

    public void setComboid(Integer comboid) {
        this.comboid = comboid;
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

}
