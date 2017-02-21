package com.movision.mybatis.cart.entity;

import com.movision.mybatis.rentdate.entity.Rentdate;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/21 16:50
 */
public class CartVo {
    private Integer id;

    private Integer userid;

    private Integer goodsid;

    private String goodsname;

    private Integer combotype;

    private Integer rom;

    private Integer num;

    private Integer takeway;

    private Integer isdebug;

    private Integer debugid;

    private Date intime;

    private Integer protype;

    private Integer isdel;

    private Integer type;//类型：0 租赁 1 购买

    private List<Rentdate> rentDateList;//租赁日期列表

    private Integer isself;//是否为三元自营：1是(自营) 0否(三方)

    private Integer shopid;//所属三方店铺id（isself为0 时不为空）-------二期预留（按照不同店铺拆分订单）

    private String imgurl;//购物车中的商品小方图

    private String comboname;//套餐名称

    private Double goodsprice;//商品折后价

    private Double comboprice;//套餐折后价

    private Integer online;//商品在线状态 0 上架 1 下架

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public Integer getCombotype() {
        return combotype;
    }

    public void setCombotype(Integer combotype) {
        this.combotype = combotype;
    }

    public Integer getRom() {
        return rom;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getTakeway() {
        return takeway;
    }

    public void setTakeway(Integer takeway) {
        this.takeway = takeway;
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

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getProtype() {
        return protype;
    }

    public void setProtype(Integer protype) {
        this.protype = protype;
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

    public String getComboname() {
        return comboname;
    }

    public void setComboname(String comboname) {
        this.comboname = comboname;
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

    public Integer getIsself() {
        return isself;
    }

    public void setIsself(Integer isself) {
        this.isself = isself;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public List<Rentdate> getRentDateList() {
        return rentDateList;
    }

    public void setRentDateList(List<Rentdate> rentDateList) {
        this.rentDateList = rentDateList;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
