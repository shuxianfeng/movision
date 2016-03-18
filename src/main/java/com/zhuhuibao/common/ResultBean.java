package com.zhuhuibao.common;

public class ResultBean {
    private String code;

    private String name;

    private String smallIcon;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }
}
