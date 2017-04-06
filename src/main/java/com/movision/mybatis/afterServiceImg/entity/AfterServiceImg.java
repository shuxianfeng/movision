package com.movision.mybatis.afterServiceImg.entity;

public class AfterServiceImg {
    private Integer afterserviceid;

    private String imgurl;

    private String width;

    private String height;

    public Integer getAfterserviceid() {
        return afterserviceid;
    }

    public void setAfterserviceid(Integer afterserviceid) {
        this.afterserviceid = afterserviceid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl == null ? null : imgurl.trim();
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width == null ? null : width.trim();
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height == null ? null : height.trim();
    }
}