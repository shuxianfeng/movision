package com.movision.mybatis.labelSearchTerms.entity;

/**
 * @Author zhanglei
 * @Date 2017/7/28 15:49
 */
public class LabelSearchTerms {
    private String id;
    private Integer labelid;

    private int userid;

    private String intime;
    private String name;

    private int isdel;
    private int type;

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }

    public Integer getLabelid() {

        return labelid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {

        return type;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }


}
