package com.movision.mybatis.compressImg.entity;

public class CompressImg {
    private String compressimgurl;

    private String protoimgurl;

    private String protoimgsize;

    public String getCompressimgurl() {
        return compressimgurl;
    }

    public void setCompressimgurl(String compressimgurl) {
        this.compressimgurl = compressimgurl == null ? null : compressimgurl.trim();
    }

    public String getProtoimgurl() {
        return protoimgurl;
    }

    public void setProtoimgurl(String protoimgurl) {
        this.protoimgurl = protoimgurl == null ? null : protoimgurl.trim();
    }

    public String getProtoimgsize() {
        return protoimgsize;
    }

    public void setProtoimgsize(String protoimgsize) {
        this.protoimgsize = protoimgsize == null ? null : protoimgsize.trim();
    }
}