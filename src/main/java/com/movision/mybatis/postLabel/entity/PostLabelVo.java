package com.movision.mybatis.postLabel.entity;

/**
 * @Author zhanglei
 * @Date 2017/7/19 19:52
 */
public class PostLabelVo {
    public Integer circleid;
    public Integer labelid;
    public String name;
    public Integer heatvalue;

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
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
