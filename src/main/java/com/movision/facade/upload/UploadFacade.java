package com.movision.facade.upload;

import com.movision.common.constant.ApiConstants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.file.FileUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 17:02
 */
@Service
@Transactional
public class UploadFacade {
    private static final Logger log = LoggerFactory.getLogger(UploadFacade.class);

    @Autowired
    private ApiConstants apiConstants;

    /**
     * 图片|文件上传 (测试环境) commons-upload
     *
     * @param file
     * @param type  目前支持三种：img/doc/video
     * @param chann 如：post，person
     * @return
     */
    public Map<String, Object> upload(MultipartFile file, String type, String chann) {
        Map<String, Object> result = new HashMap<>();
        try {
            /**
             * 这是测试环境上传的图片的路径
             * saveDirectory = /WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/upload/$chan/img
             */
            String saveDirectory;
            long maxPostSize;   //上传的文件大小
            long maxVideoDuration = 0;  //上传的视频时长
            String imgDomain = PropertiesLoader.getValue("upload.img.domain");
            String voidD = PropertiesLoader.getValue("post.video.domain");
//            String docDomain = PropertiesLoader.getValue("doc.domain");
            String fileName = FileUtil.renameFile(file.getOriginalFilename());
            Map returnMap = new HashedMap();
            String data;
            String absolutePath = null;
            switch (type) {
                case "img":
                    if (chann != null) {
                        saveDirectory = apiConstants.getUploadDir() + "/" + chann + "/img";
                        /**
                         * 这是外界访问该图片的地址
                         * 其中data = http://120.77.214.187:8100/upload/$chan/img/$filename
                         */
                        data = imgDomain + "/upload/" + chann + "/img/" + fileName;
                    } else {
                        saveDirectory = apiConstants.getUploadDir();
                        data = imgDomain + "/upload/" + fileName;
                    }
                    maxPostSize = apiConstants.getUploadPicMaxPostSize();
                    //获取图片的宽高
                    BufferedImage bi = ImageIO.read(file.getInputStream());
                    returnMap.put("width", bi.getWidth());
                    returnMap.put("height", bi.getHeight());

                    break;
                case "doc":
                    if (chann != null) {
                        saveDirectory = apiConstants.getUploadDir() + "/" + chann + "/doc";
                    } else {
                        saveDirectory = apiConstants.getUploadDir();
                    }
                    maxPostSize = apiConstants.getUploadDocMaxPostSize();
                    log.info("最大大小上传限制为=" + maxPostSize);
                    data = fileName;
                    break;
                case "video":
                    if (chann != null) {
                        saveDirectory = apiConstants.getUploadDir() + "/" + chann + "/video";
                        /**
                         * 这是外界访问该视频的地址（绝对路径）
                         * 其中data = http://120.77.214.187:8100/upload/$chan/video/$filename
                         */

                        //data = imgDomain + "/upload/" + chann + "/video/" + fileName;
                        absolutePath = imgDomain + "/upload/" + chann + "/video/" + fileName;
                        log.warn("--------------[absolutePath] = " + absolutePath);
//                        data = voidD + fileName;
                        data = voidD + "/upload/" + chann + "/video/" + fileName;
                    } else {
                        saveDirectory = apiConstants.getUploadDir();
                        data = imgDomain + "/upload/" + fileName;
                        absolutePath = data;
                    }
                    maxPostSize = apiConstants.getUploadVideoMaxPostSize();
                    log.info("最大大小上传限制为=" + maxPostSize);
                    maxVideoDuration = apiConstants.getUploadVideoMaxDuration();
                    log.info("最大视频时长上传限制为=" + maxPostSize);
                    break;

                default:
                    log.error("上传类型不支持");
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传类型不支持");
            }

            //目录不存在则创建
            createDir(saveDirectory);
            //大小校验
            fileSizeValidation(file, maxPostSize);
            //使用transferTo（dest）方法将上传文件写到服务器上指定的文件
            File upfile = new File(saveDirectory + "/" + fileName);
            file.transferTo(upfile);
            //视频时长校验
            videoDurationValidation(type, maxVideoDuration, absolutePath);

            result.put("status", "success");
            returnMap.put("url", data);
            result.put("data", returnMap);
            log.info("【上传测试服务器返回值】：" + result.toString());

        } catch (Exception e) {
            log.error("upload error!", e);
            result.put("status", "fail");
        }
        return result;
    }

    private void createDir(String saveDirectory) {
        log.info("【saveDirectory】=" + saveDirectory);
        File dir = new File(saveDirectory);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
            log.info("【mk dir susscess】, dirName = " + saveDirectory);
        }
    }

    private void fileSizeValidation(MultipartFile file, long maxPostSize) {
        long size = file.getSize();
        if (size > maxPostSize) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "文件大小超过限制");
        }
    }

    private void videoDurationValidation(String type, long maxVideoDuration, String data) throws EncoderException {
        if ("video".equals(type)) {
            Encoder encoder = new Encoder();
            File newFile = new File(data);
            MultimediaInfo m = encoder.getInfo(newFile);
            long duration = m.getDuration();    //视频时长
            if (duration > maxVideoDuration) {
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "视频时长大小超过限制");
            }
        }
    }
}
