package com.movision.mybatis.userRefreshRecord.entity;

import org.springframework.data.annotation.Id;

/**
 * @Author zhanglei
 * @Date 2017/7/19 11:50
 */
public class UesrreflushCount {

    @Id
    private Integer postid;
    private Integer count;

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
