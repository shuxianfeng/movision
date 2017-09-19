package com.movision.mybatis.postLabel.entity;

/**
 * @Author zhanglei
 * @Date 2017/7/25 14:54
 */
public class PostLabelCount {

    private Integer userid;

    private Integer id;

    private String nickname;

    private Integer count;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
