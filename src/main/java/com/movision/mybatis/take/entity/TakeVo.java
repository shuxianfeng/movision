package com.movision.mybatis.take.entity;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/8/29 16:26
 */
public class TakeVo {
    private Integer id;

    private Integer activeid;

    private String name;

    private Integer isdel;

    private Date intime;

    private Integer votesum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActiveid() {
        return activeid;
    }

    public void setActiveid(Integer activeid) {
        this.activeid = activeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public Integer getVotesum() {
        return votesum;
    }

    public void setVotesum(Integer votesum) {
        this.votesum = votesum;
    }
}
