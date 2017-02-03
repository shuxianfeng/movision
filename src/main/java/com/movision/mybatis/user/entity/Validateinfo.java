package com.movision.mybatis.user.entity;

import java.io.Serializable;

/**
 * 手机验证信息
 *
 * @Author zhuangyuhao
 * @Date 2017/2/3 18:21
 */
public class Validateinfo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String account;

    private int valid;

    private String createTime;

    private String checkCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Override
    public String toString() {
        return "Validateinfo [id=" + id + ", account=" + account + ", valid="
                + valid + ", createTime=" + createTime + ", checkCode="
                + checkCode + "]";
    }

}
