package com.movision.mybatis.cart.entity;

import java.util.Date;

public class Cart {
    private Integer id;

    private Integer userid;

    private Integer goodsid;

    private String goodsname;

    private Integer combotype;

    private Integer discountid;

    private Integer rom;

    private Integer num;

    private Integer takeway;

    private Integer isdebug;

    private Integer debugid;

    private Date intime;

    private Integer protype;

    private Integer isdel;

    private Integer type;

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
        this.goodsname = goodsname == null ? null : goodsname.trim();
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

    public Integer getDiscountid() {
        return discountid;
    }

    public void setDiscountid(Integer discountid) {
        this.discountid = discountid;
    }
}