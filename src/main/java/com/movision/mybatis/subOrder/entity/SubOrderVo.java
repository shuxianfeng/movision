package com.movision.mybatis.subOrder.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/4/5 21:06
 */
public class SubOrderVo implements Serializable {
    private Integer id;

    private Integer porderid;

    private Integer goodsid;

    private Integer combotype;

    private Integer discountid;

    private Integer rom;

    private Integer sum;

    private Integer isdebug;

    private Integer debugid;

    private Integer isdel;

    private Integer type;

    private String imgurl;//子订单中的商品小方图

    private String goodsname;//商品名称

    private Double goodsprice;//商品折后价

    private Double comboprice;//套餐折后价

    private Integer online;//商品在线状态 0 上架 1 下架

    private String discount;//活动折扣百分比

    private String comboname;//套餐名称

    private String discountname;//活动名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPorderid() {
        return porderid;
    }

    public void setPorderid(Integer porderid) {
        this.porderid = porderid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getCombotype() {
        return combotype;
    }

    public void setCombotype(Integer combotype) {
        this.combotype = combotype;
    }

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }

    public Integer getRom() {
        return rom;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getIsdebug() {
        return isdebug;
    }

    public void setIsdebug(Integer isdebug) {
        this.isdebug = isdebug;
    }

    public Integer getDebugid() {
        return debugid;
    }

    public void setDebugid(Integer debugid) {
        this.debugid = debugid;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Double getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(Double goodsprice) {
        this.goodsprice = goodsprice;
    }

    public Double getComboprice() {
        return comboprice;
    }

    public void setComboprice(Double comboprice) {
        this.comboprice = comboprice;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getComboname() {
        return comboname;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname;
    }

    public String getDiscountname() {
        return discountname;
    }

    public void setDiscountname(String discountname) {
        this.discountname = discountname;
    }
}
