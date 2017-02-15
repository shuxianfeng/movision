package com.movision.mybatis.goods.entity;

/**
 * @Author shuxf
 * @Date 2017/2/15 19:24
 */
public class GoodsImg {

    private Integer goodsid;//商品id

    private String imgurl;//图片地址url

    private Integer type;//图片类型

    private Integer oderid;//排序id

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOderid() {
        return oderid;
    }

    public void setOderid(Integer oderid) {
        this.oderid = oderid;
    }
}
