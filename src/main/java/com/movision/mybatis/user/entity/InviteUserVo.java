package com.movision.mybatis.user.entity;

import java.io.Serializable;

/**
 * @Author shuxf
 * @Date 2017/8/2 17:59
 * 邀请好友页面返回的邀请好友列表中的实体
 */
public class InviteUserVo implements Serializable {

    private Integer id;//用户id

    private String nickname;//用户昵称

    private String photo;//用户头像

    private Integer invitenum;//该用户的邀请人数

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

    public Integer getInvitenum() {
        return invitenum;
    }

    public void setInvitenum(Integer invitenum) {
        this.invitenum = invitenum;
    }
}
