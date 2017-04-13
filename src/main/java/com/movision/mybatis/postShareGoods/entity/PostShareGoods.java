package com.movision.mybatis.postShareGoods.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/2/8 15:09
 */
public class PostShareGoods implements Serializable {

    private Integer id;//主键

    private Integer postid;//帖子ID

    private Integer goodsid;//商品ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }
}
