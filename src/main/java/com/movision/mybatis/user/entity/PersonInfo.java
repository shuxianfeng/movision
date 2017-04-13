package com.movision.mybatis.user.entity;

import com.movision.utils.DateUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 个人资料
 *
 * @Author zhuangyuhao
 * @Date 2017/2/27 16:19
 */
@Api("个人资料")
public class PersonInfo implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "生日")
    private String birthday;

    @ApiModelProperty(value = "个人简介")
    private String sign;

    @ApiModelProperty(value = "头像")
    private String photo;

    @Override
    public String toString() {
        return "PersonInfo{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", sign='" + sign + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthday() {

        return birthday;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {

        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getSex() {
        return sex;
    }


    public String getSign() {
        return sign;
    }

    public String getPhoto() {
        return photo;
    }
}
