package com.movision.common.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 接口平台调用参数
 *
 * @Author zhuangyuhao
 * @Date 2017/3/20 20:34
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

    @Value("${uploadVideoMaxPostSize}")
    private Long uploadVideoMaxPostSize;

    @Value("${uploadTechMaxPostSize}")
    private Integer uploadTechMaxPostSize;

    public void setUploadVideoMaxPostSize(Long uploadVideoMaxPostSize) {
        this.uploadVideoMaxPostSize = uploadVideoMaxPostSize;
    }

    public Long getUploadVideoMaxPostSize() {

        return uploadVideoMaxPostSize;
    }

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
