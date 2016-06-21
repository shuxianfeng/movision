package com.zhuhuibao.utils.oss;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author jianglz
 * @since 16/6/21.
 */
@Service
public class ZhbOssClient {
    private static final Logger log = LoggerFactory.getLogger(AliOSSClient.class);

    @Autowired
    UploadService uploadService;

    @Autowired
    AliOSSClient aliOSSClient;

    public String uploadObject( MultipartFile file,String type,String chann){
        String uploadMode = PropertiesUtils.getValue("upload.mode");
        switch (uploadMode) {
            case "alioss":
                Map<String, String> map = aliOSSClient.uploadFileStream(file, type,chann);
                String status = map.get("status");
                if (status.equals("success")) {
                    return map.get("data");
                } else {
                    log.error("上传失败");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传失败");
                }

            case "zhb":
                Map<String, String> map2 =  uploadService.upload(file,type,chann);
                return map2.get("data");
            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }
}
