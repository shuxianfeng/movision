package com.movision.mybatis.category.entity;

/**
 * @Author zhurui
 * @Date 2017/2/22 19:31
 */
public class CircleAndCircle {
    private Integer circleid;//圈子id
    private String circlename;//圈子名称
    private String nickname;//圈主
    private Integer userid;//圈主id

    public Integer getCircleid() {
        return circleid;
    }

    public void setCircleid(Integer circleid) {
        this.circleid = circleid;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
