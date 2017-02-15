package com.movision.mybatis.user.entity;

import java.io.Serializable;
import java.util.Date;

public class UserLike implements Serializable {

    private Integer id;

    private String nickname;

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
}