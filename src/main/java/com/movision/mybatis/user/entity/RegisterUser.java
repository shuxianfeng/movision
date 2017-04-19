package com.movision.mybatis.user.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/3 17:45
 */
@ApiModel(value = "app注册用户", description = "app注册用户")
public class RegisterUser implements Serializable {
    private Integer id;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    private String account;

    @ApiModelProperty(value = "手机短信验证码", required = true)
    private String mobileCheckCode;

    @ApiModelProperty(value = "注册时间")
    private Date intime;

    @ApiModelProperty(value = "账号状态：默认 0 正常 1 异常封号")
    private Integer status;

    @ApiModelProperty(value = "用户身份令牌")
    private String token;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "设备号", required = true)
    private String deviceno;

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {

        return nickname;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {

        return token;
    }

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

    @Override
    public String toString() {
        return "RegisterUser{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", account='" + account + '\'' +
                ", mobileCheckCode='" + mobileCheckCode + '\'' +
                ", intime=" + intime +
                ", status=" + status +
                ", token='" + token + '\'' +
                ", nickname='" + nickname + '\'' +
                ", deviceno='" + deviceno + '\'' +
                '}';
    }
}
