package com.movision.mybatis.followUser.entity;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/2 15:56
 */
public class FollowUserVo {

    private Integer id;

    private Integer userid; //关注的主体，A关注B，指的是A
    private String username;    //A的名称
    private String userphoto;   //A的头像

    private Integer interestedusers;    //被关注的用户id， 即B

    private Integer isFollow;   //是否关注对方，即B是否关注A。 0：未关注； 1：已关注

    private Date intime;
    private Integer isread; //是否已读，0：未读，1：已读

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setInterestedusers(Integer interestedusers) {
        this.interestedusers = interestedusers;
    }


    public void setIsFollow(Integer isFollow) {
        this.isFollow = isFollow;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }


    public Integer getUserid() {
        return userid;
    }

    public Integer getInterestedusers() {
        return interestedusers;
    }

    public void setIsread(Integer isread) {
        this.isread = isread;
    }

    public Integer getIsread() {

        return isread;
    }

    public Integer getIsFollow() {
        return isFollow;
    }

    public Date getIntime() {
        return intime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUsername() {

        return username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    @Override
    public String toString() {
        return "FollowUserVo{" +
                "id=" + id +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", userphoto='" + userphoto + '\'' +
                ", interestedusers=" + interestedusers +
                ", isFollow=" + isFollow +
                ", intime=" + intime +
                ", isread=" + isread +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {

        return id;
    }
}
