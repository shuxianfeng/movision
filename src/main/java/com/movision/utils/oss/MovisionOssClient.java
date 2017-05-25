package com.movision.utils.oss;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.facade.upload.UploadFacade;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * 文件上传
 *
 * @author zhuangyuhao
 */
@Service
public class MovisionOssClient {
    private static final Logger log = LoggerFactory.getLogger(AliOSSClient.class);

    @Autowired
    private AliOSSClient aliOSSClient;

    @Autowired
    private UploadFacade uploadFacade;

    /**
     * 图片文件上传(文件流)
     *
     * @param file  multipartfile
     * @param type  img | doc | video
     * @param chann 频道类型  post | person | ....
     * @return url | filename
     */
    public Map<String, Object> uploadObject(MultipartFile file, String type, String chann) {

        String uploadMode = PropertiesLoader.getValue("upload.mode");
        //判断是否为允许的上传文件后缀
        boolean allowed = FileUtil.isAllowed(file.getOriginalFilename(), type);
        if (!allowed) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不允许的上传类型");
        }

        switch (uploadMode) {
            case "alioss":
                Map<String, Object> map = aliOSSClient.uploadFileStream(file, type, chann);
                String status = String.valueOf(map.get("status"));
                if (status.equals("success")) {
                    return map;
                } else {
                    log.error("上传失败");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传失败");
                }

            case "movision":
                // 上传到测试服务器，返回url
                log.debug("上传到测试服务器");
                Map<String, Object> map2 = uploadFacade.upload(file, type, chann);
                Map<String, Object> dataMap = (Map<String, Object>) map2.get("data");
                log.info("【dataMap】=" + dataMap);
                return dataMap;

            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }

    /**
     * 图片文件上传（上传本地图片）
     *
     * @param file  file
     * @param type  img | doc | video
     * @param chann 频道类型  post | person | ....
     * @return url | filename
     */
    public Map<String, String> uploadFileObject(File file, String type, String chann) {

        String uploadMode = PropertiesLoader.getValue("upload.mode");
        //判断是否为允许的上传文件后缀
        boolean allowed = FileUtil.isAllowed(file.getName(), type);
        if (!allowed) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不允许的上传类型");
        }

        switch (uploadMode) {
            case "alioss":
                Map<String, String> map = aliOSSClient.uploadLocalFile(file, type, chann);
                String status = String.valueOf(map.get("status"));
                if (status.equals("success")) {
                    return map;
                } else {
                    log.error("上传失败");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传失败");
                }

            case "movision":
                // 上传到测试服务器，返回url--------------------------由于uploadFacade.upload()接口中目前只实现了文件流，并不支持本地文件流上传，临时屏蔽20170525 shuxf
                log.debug("上传到测试服务器");
//                Map<String, Object> map2 = uploadFacade.upload(file, type, chann);
//                Map<String, Object> dataMap = (Map<String, Object>) map2.get("data");
//                log.info("【dataMap】=" + dataMap);
//                return dataMap;

            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }
}
