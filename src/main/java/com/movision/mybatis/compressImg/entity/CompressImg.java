package com.movision.mybatis.compressImg.entity;

import java.io.Serializable;
import java.util.Date;

public class CompressImg implements Serializable {
    private String compressimgurl;

    private String protoimgurl;

    private String protoimgsize;

    private Date intime;

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

    public Date getIntime() {
        return intime;
    }

    public void setIntime(Date intime) {
        this.intime = intime;
    }
}