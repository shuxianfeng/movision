package com.movision.mybatis.userRefreshRecord.entity;

/**
 * @Author zhanglei
 * @Date 2017/6/14 16:02
 */
public class UserRefreshRecord {

    private String id;

    private int userid;

    private int postid;

    private String crileid;

    private String intime;

    private int type;//1：推荐2：关注3：本地 4：圈子 5：标签
    private String device;

    private int labelid;

    public int getLabelid() {
        return labelid;
    }

    public void setLabelid(int labelid) {
        this.labelid = labelid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCrileid() {
        return crileid;
    }

    public void setCrileid(String crileid) {
        this.crileid = crileid;
    }
}
