package com.movision.mybatis.postLabel.entity;

/**
 * @Author zhanglei
 * @Date 2017/7/19 19:52
 */
public class PostLabelVo {
    public Integer id;
    public String name;
    public Integer heatvalue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHeatvalue() {
        return heatvalue;
    }

    public void setHeatvalue(Integer heatvalue) {
        this.heatvalue = heatvalue;
    }
}
