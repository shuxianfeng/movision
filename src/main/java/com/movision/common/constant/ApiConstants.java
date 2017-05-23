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
    /**
     * 上传文件的服务器的绝对路径
     */
    @Value("${uploadDir}")
    private  String uploadDir;
//    @Value("${uploadDoc}")
//    private String uploadDoc;

    /**
     * 图片最大大小
     */
    @Value("${uploadPicMaxPostSize}")
    private int uploadPicMaxPostSize;

    /**
     * 文件最大大小
     */
    @Value("${uploadDocMaxPostSize}")
    private int uploadDocMaxPostSize;

    /**
     * 视频最大大小（单位：B）
     */
    @Value("${uploadVideoMaxPostSize}")
    private Long uploadVideoMaxPostSize;

    /**
     * 视频最大时长（单位：毫秒）
     */
    @Value("${uploadVideoMaxDuration}")
    private Long uploadVideoMaxDuration;


    public void setUploadVideoMaxDuration(Long uploadVideoMaxDuration) {
        this.uploadVideoMaxDuration = uploadVideoMaxDuration;
    }

    public Long getUploadVideoMaxDuration() {

        return uploadVideoMaxDuration;
    }

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

//    public String getUploadDoc() {
//        return uploadDoc;
//    }
//
//    public void setUploadDoc(String uploadDoc) {
//        this.uploadDoc = uploadDoc;
//    }

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


}
