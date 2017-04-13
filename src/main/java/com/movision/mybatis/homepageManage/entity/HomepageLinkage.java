package com.movision.mybatis.homepageManage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用于查询出的广告位置和排序联动
 *
 * @Author zhurui
 * @Date 2017/3/24 14:45
 */
public class HomepageLinkage implements Serializable {

    private String location;//广告位置

    private List<Integer> sort;//排序

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Integer> getSort() {
        return sort;
    }

    public void setSort(List<Integer> sort) {
        this.sort = sort;
    }
}
