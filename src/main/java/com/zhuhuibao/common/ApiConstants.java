package com.zhuhuibao.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 接口平台调用参数
 * Created by cxx
 */
@Component
public class ApiConstants {
    @Value("${uploadDir}")
    private  String uploadDir;

    @Value("${uploadMaxPostSize}")
    private int uploadMaxPostSize;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public int getUploadMaxPostSize() {
        return uploadMaxPostSize;
    }

    public void setUploadMaxPostSize(int uploadMaxPostSize) {
        this.uploadMaxPostSize = uploadMaxPostSize;
    }
}
