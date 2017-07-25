package com.movision.mybatis.postLabelRelation.entity;

/**
 * @Author zhanglei
 * @Date 2017/7/24 18:21
 */
public class PostLabelRelationVo {
    private Integer id;

    private Integer labelid;

    private Integer postid;

    private Integer count;

    private String name;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }

    public Integer getPostid() {
        return postid;
    }

    public void setPostid(Integer postid) {
        this.postid = postid;
    }
}
