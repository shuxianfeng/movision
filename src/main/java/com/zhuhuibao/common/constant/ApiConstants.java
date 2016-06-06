package com.zhuhuibao.common.constant;

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
    @Value("${uploadDoc}")
    private String uploadDoc;

    @Value("${uploadPicMaxPostSize}")
    private int uploadPicMaxPostSize;

    @Value("${uploadDocMaxPostSize}")
    private int uploadDocMaxPostSize;

    @Value("${uploadTechMaxPostSize}")
    private Integer uploadTechMaxPostSize;


    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getUploadDoc() {
        return uploadDoc;
    }

    public void setUploadDoc(String uploadDoc) {
        this.uploadDoc = uploadDoc;
    }

    public int getUploadPicMaxPostSize() {
        return uploadPicMaxPostSize;
    }

    public void setUploadPicMaxPostSize(int uploadPicMaxPostSize) {
        this.uploadPicMaxPostSize = uploadPicMaxPostSize;
    }

    public int getUploadDocMaxPostSize() {
        return uploadDocMaxPostSize;
    }

    public void setUploadDocMaxPostSize(int uploadDocMaxPostSize) {
        this.uploadDocMaxPostSize = uploadDocMaxPostSize;
    }

    public Integer getUploadTechMaxPostSize() {
        return uploadTechMaxPostSize;
    }

    public void setUploadTechMaxPostSize(Integer uploadTechMaxPostSize) {
        this.uploadTechMaxPostSize = uploadTechMaxPostSize;
    }
}
