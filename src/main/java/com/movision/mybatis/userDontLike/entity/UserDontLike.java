package com.movision.mybatis.userDontLike.entity;

/**
 * @Author zhanglei
 * @Date 2017/8/2 9:32
 */
public class UserDontLike {

    private String id;
    private String intime;
    private int userid;

    private int postid;

    private int type;//1 内容差 2 不喜欢作者 3 不喜欢圈子 4 就是不喜欢

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
