package com.movision.mybatis.circle.entity;

/**
 * 用于接受，返回根据圈子类型关注数,今日新增关注人数
 *
 * @Author zhurui
 * @Date 2017/2/15 19:19
 */
public class CircleFollowNum {

    private Integer num;

    private Integer newnum;

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getNewnum() {
        return newnum;
    }

    public void setNewnum(Integer newnum) {
        this.newnum = newnum;
    }
}
