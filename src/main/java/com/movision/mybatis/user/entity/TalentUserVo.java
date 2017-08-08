package com.movision.mybatis.user.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/8/4 11:37
 */
public class TalentUserVo implements Serializable{

    private Integer id;//用户id

    private String nickname;//用户昵称

    private String photo;//头像

    private Integer level;//用户等级

    private Double lackxp;//升到下一级需要的经验值

    private Double rate;//当前拥有的经验所占百分比

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getLackxp() {
        return lackxp;
    }

    public void setLackxp(Double lackxp) {
        this.lackxp = lackxp;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
