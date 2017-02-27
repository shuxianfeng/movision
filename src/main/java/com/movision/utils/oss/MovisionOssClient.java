package com.movision.utils.oss;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.facade.upload.UploadFacade;
import com.movision.utils.PropertiesUtils;
import com.movision.utils.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
     * 图片文件上传
     *
     * @param file  multipartfile
     * @param type  img | doc
     * @param chann 频道类型  post | person | ....
     * @return url | filename
     */
    public String uploadObject(MultipartFile file, String type, String chann) {

        String uploadMode = PropertiesUtils.getValue("upload.mode");
        //判断是否为允许的上传文件后缀
        boolean allowed = FileUtil.isAllowed(file.getOriginalFilename(), type);
        if (!allowed) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不允许的上传类型");
        }

        switch (uploadMode) {
            case "alioss":
                Map<String, String> map = aliOSSClient.uploadFileStream(file, type, chann);
                String status = map.get("status");
                if (status.equals("success")) {
                    return map.get("data");
                } else {
                    log.error("上传失败");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传失败");
                }

            case "movision":
                // 上传到测试服务器，返回url
                Map<String, String> map2 = uploadFacade.upload(file, type, chann);
                return map2.get("data");

            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }


}
