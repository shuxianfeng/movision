package com.zhuhuibao.mybatis.common.entity;

public class SendMsgMobile {
    private String moible;

    private String name;

    public String getMoible() {
        return moible;
    }

    public void setMoible(String moible) {
        this.moible = moible == null ? null : moible.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}