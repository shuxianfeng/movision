package com.movision.mybatis.homepageManage.entity;

import java.io.Serializable;
import java.util.Date;

public class HomepageManage implements Serializable {
    private Integer id;

    //广告位置：0 首页--你可能喜欢--板块banner  1 ‘发现’首页banner
    //2 首页最上方的主banner 3 商城--月度热销banner
    //4 商城--一周热销banner  5 商城--每日神器推荐banner',
    private Integer topictype;

    private String content;//内容主标题

    private String subcontent;//内容副标题

    private String url;//图片url

    private String transurl;//跳转url

    private Integer orderid;//排序id

    private Date intime;//主题创建时间

    private Integer clicksum;//点击次数

    private Integer ordersum;//生成订单数量

    private Integer isdel;//是否删除 1是 0否

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getClicksum() {
        return clicksum;
    }

    public void setClicksum(Integer clicksum) {
        this.clicksum = clicksum;
    }

    public Integer getOrdersum() {
        return ordersum;
    }

    public void setOrdersum(Integer ordersum) {
        this.ordersum = ordersum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTopictype() {
        return topictype;
    }

    public void setTopictype(Integer topictype) {
        this.topictype = topictype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getSubcontent() {
        return subcontent;
    }

    public void setSubcontent(String subcontent) {
        this.subcontent = subcontent == null ? null : subcontent.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getTransurl() {
        return transurl;
    }

    public void setTransurl(String transurl) {
        this.transurl = transurl == null ? null : transurl.trim();
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }
}