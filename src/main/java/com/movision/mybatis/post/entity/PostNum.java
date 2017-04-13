package com.movision.mybatis.post.entity;

import java.io.Serializable;

/**
 * 用于返回帖子总数和精选帖子数
 *
 * @Author zhurui
 * @Date 2017/2/9 14:36
 */
public class PostNum implements Serializable {
    private Integer postnum;
    private Integer isessence;

    public Integer getPostnum() {
        return postnum;
    }

    public void setPostnum(Integer postnum) {
        this.postnum = postnum;
    }

    public Integer getIsessence() {
        return isessence;
    }

    public void setIsessence(Integer isessence) {
        this.isessence = isessence;
    }
}
