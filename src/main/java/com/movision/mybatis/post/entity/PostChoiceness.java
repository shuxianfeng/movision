package com.movision.mybatis.post.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用于帖子加精返回数据回显
 *
 * @Author zhurui
 * @Date 2017/2/18 17:11
 */
public class PostChoiceness {
    private Integer id;//帖子id

    private String subtitle;//帖子副标题

    private Date essencedate;//精选日期

    private Map<String, Object> orderid;//精选排序

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

    public Map<String, Object> getOrderid() {
        return orderid;
    }

    public void setOrderid(Map<String, Object> orderid) {
        this.orderid = orderid;
    }
}
