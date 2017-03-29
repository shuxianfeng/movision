package com.movision.mybatis.bossIndex.entity;

/**
 * 后台系统首页上方的统计
 *
 * @Author zhurui
 * @Date 2017/3/29 11:16
 */
public class AboveStatistics {

    private Integer posts;//帖子数
    private Integer orders;//订单数
    private Integer goods;//商品数
    private Integer vip;//vip数
    private Integer quintessence;//精选池

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Integer getGoods() {
        return goods;
    }

    public void setGoods(Integer goods) {
        this.goods = goods;
    }

    public Integer getVip() {
        return vip;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public Integer getQuintessence() {
        return quintessence;
    }

    public void setQuintessence(Integer quintessence) {
        this.quintessence = quintessence;
    }
}
