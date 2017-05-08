package com.movision.mybatis.opularSearchTerms.entity;

   import com.mongodb.Mongo;
   import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @Author zhanglei
 * @Date 2017/4/18 17:57
 */
public class OpularSearchTerms {

    @Id
    private String id;

    private Integer isdel;

    private String keywords;

    private String intime;

    private Integer userid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
