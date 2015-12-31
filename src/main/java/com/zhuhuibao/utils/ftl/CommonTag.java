package com.zhuhuibao.utils.ftl;

import org.springframework.stereotype.Service;

@Service
public class CommonTag {

    private String hostRes;

    private String hostRoot;

    public String getHostRes() {
        return hostRes;
    }

    public void setHostRes(String hostRes) {
        this.hostRes = hostRes;
    }

    public String getHostRoot() {
        return hostRoot;
    }

    public void setHostRoot(String hostRoot) {
        this.hostRoot = hostRoot;
    }

    public String res(String path) {
        return path == null ? hostRes : hostRes + path;
    }
}
