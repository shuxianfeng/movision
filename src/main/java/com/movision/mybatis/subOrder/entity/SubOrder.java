package com.movision.mybatis.subOrder.entity;

public class SubOrder {
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
}