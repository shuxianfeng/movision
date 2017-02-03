package com.movision.mybatis.user.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/3 17:45
 */
public class RegisterUser implements Serializable {
    private Integer id;
    private String phone;
    private String account;
    private String mobileCheckCode;
    private Date intime;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccount() {
        return account;
    }

    public String getMobileCheckCode() {
        return mobileCheckCode;
    }

    public Date getIntime() {
        return intime;
    }

    public Integer getStatus() {
        return status;
    }


    public void setId(Integer id) {

        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMobileCheckCode(String mobileCheckCode) {
        this.mobileCheckCode = mobileCheckCode;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
