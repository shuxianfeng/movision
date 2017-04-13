package com.movision.mybatis.circle.entity;

import java.io.Serializable;

/**
 * 用于接收，返回帖子数量 ，今日新增帖子数量，精贴数量
 *
 * @Author zhurui
 * @Date 2017/2/15 19:21
 */
public class CirclePostNum implements Serializable {
    private Integer postnum;

    private Integer newpostnum;

    private Integer isessencenum;

    public Integer getPostnum() {
        return postnum;
    }

    public void setPostnum(Integer postnum) {
        this.postnum = postnum;
    }

    public Integer getNewpostnum() {
        return newpostnum;
    }

    public void setNewpostnum(Integer newpostnum) {
        this.newpostnum = newpostnum;
    }

    public Integer getIsessencenum() {
        return isessencenum;
    }

    public void setIsessencenum(Integer isessencenum) {
        this.isessencenum = isessencenum;
    }
}
