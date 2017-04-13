package com.movision.mybatis.goods.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/2/15 19:24
 */
public class GoodsImg implements Serializable {

    private Integer goodsid;//商品id

    private String imgurl;//图片地址url

    private Integer type;//图片类型

    private Integer oderid;//排序id

    private String width;

    private String height;

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
