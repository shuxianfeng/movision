package com.movision.mybatis.post.entity;

/**
 * @Author zhanglei
 * @Date 2017/11/15 17:04
 */
public class PostReturnAll {

    private Integer id;
    private String nickname;

    private String photo;

    private Integer iszan;

    private String cirlename;

    private Integer countview;

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

    public Integer getIszan() {
        return iszan;
    }

    public void setIszan(Integer iszan) {
        this.iszan = iszan;
    }

    public String getCirlename() {
        return cirlename;
    }

    public void setCirlename(String cirlename) {
        this.cirlename = cirlename;
    }

    public Integer getCountview() {
        return countview;
    }

    public void setCountview(Integer countview) {
        this.countview = countview;
    }
}
