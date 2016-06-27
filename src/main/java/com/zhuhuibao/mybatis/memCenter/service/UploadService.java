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
    ApiConstants ApiConstants;

    public Map<String, String> upload(HttpServletRequest req, String type, String chann) {
        Map<String, String> result = new HashMap<>();

        try {
            String saveDirectory;
            int maxPostSize;
            String host = PropertiesUtils.getValue("host.ip");
//            String fileName = FileUtil.renameFile(file.getOriginalFilename());
            String data = "";
            switch (type) {
                case "img":
                    if (chann != null) {
                        saveDirectory = ApiConstants.getUploadDir() + "/" + chann + "/img";
                        maxPostSize = ApiConstants.getUploadPicMaxPostSize();
                        data = host + "/upload/" + chann + "/img/";
                    } else {
                        saveDirectory = ApiConstants.getUploadDir();
                        maxPostSize = ApiConstants.getUploadPicMaxPostSize();
                        data = host + "/upload/";
                    }

                    break;
                case "doc":
                    if (chann != null) {
                        saveDirectory = ApiConstants.getUploadDoc() + "/" + chann + "/doc";
                        maxPostSize = ApiConstants.getUploadDocMaxPostSize();
                        if (chann.equals("tech")) {
                            maxPostSize = ApiConstants.getUploadTechMaxPostSize();
                        }
                    } else {
                        saveDirectory = ApiConstants.getUploadDoc();
                        maxPostSize = ApiConstants.getUploadDocMaxPostSize();
                    }
//                    data = fileName;
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

//            long size = file.getSize();
//            if (size > maxPostSize) {
//                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "文件大小超过限制");
//            }
//
//
//            File upfile = new File(saveDirectory + "/" + fileName);
//            file.transferTo(upfile);
            //指定所上传的文件命名规则
            RandomFileNamePolicy rfnp = new RandomFileNamePolicy();
            //完成文件上传
            MultipartRequest multi = new MultipartRequest(req, saveDirectory, maxPostSize, "UTF-8", rfnp);

            Enumeration fileNames = multi.getFileNames();
            String lastFileName  = "";
            while (fileNames.hasMoreElements()) {
                String fileName = (String) fileNames.nextElement();
                if (null != multi.getFile(fileName)) {
                    lastFileName = multi.getFilesystemName(fileName);
                }
            }

            if("img".equals(type)){
               data = data + lastFileName;
            }else if("doc".equals(type)){
                data = lastFileName;
            }

            result.put("status", "success");
            result.put("data", data);

        } catch (Exception e) {
            log.error("upload error!", e);

            result.put("status", "fail");

        }
        return result;
    }


    public Map<String, String> upload(MultipartFile file, String type, String chann) {
        Map<String, String> result = new HashMap<>();

        try {
            String saveDirectory;
            int maxPostSize;
            String host = PropertiesUtils.getValue("host.ip");
            String fileName = FileUtil.renameFile(file.getOriginalFilename());
            String data;
            switch (type) {
                case "img":
                    if (chann != null) {
                        saveDirectory = ApiConstants.getUploadDir() + "/" + chann + "/img";
                        maxPostSize = ApiConstants.getUploadPicMaxPostSize();
                        data = host + "/upload/" + chann + "/img/" + fileName;
                    } else {
                        saveDirectory = ApiConstants.getUploadDir();
                        maxPostSize = ApiConstants.getUploadPicMaxPostSize();
                        data = host + "/upload/" + fileName;
                    }

                    break;
                case "doc":
                    if (chann != null) {
                        saveDirectory = ApiConstants.getUploadDoc() + "/" + chann + "/doc";
                        maxPostSize = ApiConstants.getUploadDocMaxPostSize();
                        if (chann.equals("tech")) {
                            maxPostSize = ApiConstants.getUploadTechMaxPostSize();
                        }
                    } else {
                        saveDirectory = ApiConstants.getUploadDoc();
                        maxPostSize = ApiConstants.getUploadDocMaxPostSize();
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
     * cos插件
     * 上传图片，返回url
     */
    public String upload(HttpServletRequest req, String type) throws IOException {
        //指定所上传的文件，上传成功后，在服务器的保存位置
        String url = "";
        try {
            String saveDirectory;
            int maxPostSize;
            if ("img".equals(type)) {
                saveDirectory = ApiConstants.getUploadDir() + "/img";
                //指定所上传的文件最大上传文件大小
                maxPostSize = ApiConstants.getUploadPicMaxPostSize();
            } else if ("doc".equals(type)) {
                saveDirectory = ApiConstants.getUploadDoc() + "/price";
                //指定所上传的文件最大上传文件大小
                maxPostSize = ApiConstants.getUploadDocMaxPostSize();
            } else if ("project".equals(type)) {
                saveDirectory = ApiConstants.getUploadDoc() + "/project";
                //指定所上传的文件最大上传文件大小
                maxPostSize = ApiConstants.getUploadDocMaxPostSize();
            } else if ("techdoc".equals(type)) {
                //技术资料
                saveDirectory = ApiConstants.getUploadDoc() + "/tech/doc";
                //技术资料最大1G
                maxPostSize = ApiConstants.getUploadTechMaxPostSize();
            } else if ("techimg".equals(type)) {
                saveDirectory = ApiConstants.getUploadDir() + "/tech/img";
                //指定所上传的文件最大上传文件大小
                maxPostSize = ApiConstants.getUploadPicMaxPostSize();
            } else if ("expert".equals(type)) {
                //技术资料
                saveDirectory = ApiConstants.getUploadDir() + "/expert";
                //技术资料最大1G
                maxPostSize = ApiConstants.getUploadPicMaxPostSize();
            } else {
                saveDirectory = ApiConstants.getUploadDoc() + "/job";
                maxPostSize = ApiConstants.getUploadDocMaxPostSize();
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
        }
        return url;
    }


}
