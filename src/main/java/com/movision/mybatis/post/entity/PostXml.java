package com.movision.mybatis.post.entity;

import java.io.Serializable;

public class PostXml implements Serializable {
    private Integer id;//帖子id

    private Integer userid;//用户id

    private String circlename;//圈子名称

    private String title;//标题

    private String subtitle;//副标题

    private String postcontent;//帖子内容

    private String coverimg;//帖子封面

    private String nickname;//用户昵称

    private String phone;//手机号

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCirclename() {
        return circlename;
    }

    public void setCirclename(String circlename) {
        this.circlename = circlename;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostcontent() {
        return postcontent;
    }

    public void setPostcontent(String postcontent) {
        this.postcontent = postcontent;
    }

    public String getCoverimg() {
        return coverimg;
    }

    public void setCoverimg(String coverimg) {
        this.coverimg = coverimg;
    }
}