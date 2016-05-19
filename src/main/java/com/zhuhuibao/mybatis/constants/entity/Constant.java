package com.zhuhuibao.mybatis.constants.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value="常量",description = "常量")
public class Constant implements Serializable {
    @ApiModelProperty(value="ID")
    private Integer id;

    @ApiModelProperty(value="code")
    private Integer code;

    @ApiModelProperty(value="name")
    private String name;

    @ApiModelProperty(value="type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}