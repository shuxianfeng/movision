package com.movision.mybatis.systemInformReadRecord.entity;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/9/5 15:00
 */
public class SystemInformReadRecord {
    @Id
    private String id;

    private Integer userid;

    private Date intime;

    private String informIdentity;


    @Override
    public String toString() {
        return "SystemInformReadRecord{" +
                "id=" + id +
                ", userid=" + userid +
                ", intime=" + intime +
                ", informIdentity='" + informIdentity + '\'' +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setInformIdentity(String informIdentity) {
        this.informIdentity = informIdentity;
    }



    public Integer getUserid() {
        return userid;
    }

    public Date getIntime() {
        return intime;
    }

    public String getInformIdentity() {
        return informIdentity;
    }


}
