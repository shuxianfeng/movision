package com.movision.mybatis.post.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用于帖子加精返回数据回显
 *
 * @Author zhurui
 * @Date 2017/2/18 17:11
 */
public class PostChoiceness implements Serializable {
    private Integer id;//帖子id

    private String subtitle;//帖子副标题

    private Date essencedate;//精选日期

    private Integer orderid;//精选排序

    private Map<String, List> orderids;//精选排序集合

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Date getEssencedate() {
        return essencedate;
    }

    public void setEssencedate(Date essencedate) {
        this.essencedate = essencedate;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Map<String, List> getOrderids() {
        return orderids;
    }

    public void setOrderids(Map<String, List> orderids) {
        this.orderids = orderids;
    }
}
