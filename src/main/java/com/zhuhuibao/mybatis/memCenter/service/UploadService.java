package com.zhuhuibao.mybatis.memCenter.service;


import com.oreilly.servlet.MultipartRequest;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.utils.RandomFileNamePolicy;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 上传处理
 *
 * @author cxx
 */
@Service
@Transactional
public class UploadService {
    private static final Logger log = LoggerFactory.getLogger(UploadService.class);

    @Autowired
    ApiConstants apiConstants;


    /**
     * 图片|文件上传 (测试环境) commons-upload
     * @param file
     * @param type
     * @param chann
     * @return
     */
    public Map<String, String> upload(MultipartFile file, String type, String chann) {
        Map<String, String> result = new HashMap<>();
        try {
        	/**
        	 * saveDirectory = /home/app/upload/$chan/img
        	 */
            String saveDirectory;
            int maxPostSize;
            String imgDomain = PropertiesUtils.getValue("img.domain");
//            String docDomain = PropertiesUtils.getValue("doc.domain");
            String fileName = FileUtil.renameFile(file.getOriginalFilename());
            String data;
            switch (type) {
                case "img":
                    if (chann != null) {
                        saveDirectory = apiConstants.getUploadDir() + "/" + chann + "/img";
                        maxPostSize = apiConstants.getUploadPicMaxPostSize();
                        /**
                         * 其中data = //image.zhuhui8.com/upload/$chan/img/$filename
                         */
                        data = "//"+imgDomain + "/upload/" + chann + "/img/" + fileName;
                    } else {
                        saveDirectory = apiConstants.getUploadDir();
                        maxPostSize = apiConstants.getUploadPicMaxPostSize();
                        data = "//"+imgDomain + "/upload/" + fileName;
                    }

                    break;
                case "doc":
                    if (chann != null) {
                        saveDirectory = apiConstants.getUploadDir() + "/" + chann + "/doc";
                        maxPostSize = apiConstants.getUploadDocMaxPostSize();
                        if (chann.equals("tech")) {
                            maxPostSize = apiConstants.getUploadTechMaxPostSize();
                        }
                    } else {
                        saveDirectory = apiConstants.getUploadDir();
                        maxPostSize = apiConstants.getUploadDocMaxPostSize();
                    }
                    data = fileName;
                    break;
                default:
                    log.error("上传类型不支持");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传类型不支持");
            }


            //目录不存在则创建
            File dir = new File(saveDirectory);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
                log.info("mk dir susscess dirName = " + saveDirectory);
            }

            long size = file.getSize();
            if (size > maxPostSize) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "文件大小超过限制");
            }


            File upfile = new File(saveDirectory + "/" + fileName);
            //使用transferTo（dest）方法将上传文件写到服务器上指定的文件
            file.transferTo(upfile);


            result.put("status", "success");
            result.put("data", data);

        } catch (Exception e) {
            log.error("upload error!", e);
            result.put("status", "fail");

        }
        return result;
    }


    /**
     * cos插件 {废弃}
     * 上传图片，返回url
     */
    @Deprecated
    public String upload(HttpServletRequest req, String type) throws IOException {
        //指定所上传的文件，上传成功后，在服务器的保存位置
        String url = "";
        try {
            String saveDirectory;
            int maxPostSize;
            if ("img".equals(type)) {
                saveDirectory = apiConstants.getUploadDir() + "/img";
                //指定所上传的文件最大上传文件大小
                maxPostSize = apiConstants.getUploadPicMaxPostSize();
            } else if ("doc".equals(type)) {
                saveDirectory = apiConstants.getUploadDoc() + "/price";
                //指定所上传的文件最大上传文件大小
                maxPostSize = apiConstants.getUploadDocMaxPostSize();
            } else if ("project".equals(type)) {
                saveDirectory = apiConstants.getUploadDoc() + "/project";
                //指定所上传的文件最大上传文件大小
                maxPostSize = apiConstants.getUploadDocMaxPostSize();
            } else if ("techdoc".equals(type)) {
                //技术资料
                saveDirectory = apiConstants.getUploadDoc() + "/tech/doc";
                //技术资料最大1G
                maxPostSize = apiConstants.getUploadTechMaxPostSize();
            } else if ("techimg".equals(type)) {
                saveDirectory = apiConstants.getUploadDir() + "/tech/img";
                //指定所上传的文件最大上传文件大小
                maxPostSize = apiConstants.getUploadPicMaxPostSize();
            } else if ("expert".equals(type)) {
                //技术资料
                saveDirectory = apiConstants.getUploadDir() + "/expert";
                //技术资料最大1G
                maxPostSize = apiConstants.getUploadPicMaxPostSize();
            } else {
                saveDirectory = apiConstants.getUploadDoc() + "/job";
                maxPostSize = apiConstants.getUploadDocMaxPostSize();
            }
            //目录不存在则创建
            File dir = new File(saveDirectory);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
                log.info("mk dir susscess dirName = " + saveDirectory);
            }
            //String a = getFileSuffix(req);

            String ip_address = PropertiesUtils.getValue("host.ip");

            //指定所上传的文件命名规则
            RandomFileNamePolicy rfnp = new RandomFileNamePolicy();

            //完成文件上传
            MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8", rfnp);

            Enumeration fileNames = multi.getFileNames();

            while (fileNames.hasMoreElements()) {
                String fileName = (String) fileNames.nextElement();
                if (null != multi.getFile(fileName)) {
                    String lastFileName = multi.getFilesystemName(fileName);
                    if ("img".equals(type)) {
                        url = ip_address + "/upload/img/" + lastFileName;
                    } else if ("techimg".equals(type)) {
                        url = ip_address + "/upload/tech/img/" + lastFileName;
                    } else {
                        url = lastFileName;
                    }

                }
            }
        } catch (Exception e) {
            log.error("upload error!", e);
            throw e;
        }
        return url;
    }


}
