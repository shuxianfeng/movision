package com.movision.utils.oss;

import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.facade.upload.UploadFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.file.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

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
                String domain = PropertiesLoader.getValue("formal.img.domain");

                return aliossUpload(file, type, chann, domain);

            case "movision":
                // 上传到测试服务器，返回url
//                log.debug("上传到测试服务器");
//                Map<String, Object> map2 = uploadFacade.upload(file, type, chann);
//                Map<String, Object> dataMap = (Map<String, Object>) map2.get("data");
//                log.info("【dataMap】=" + dataMap);
//                return dataMap;

                //还是上传到alioss，只是域名不同
                String domain2 = PropertiesLoader.getValue("test.img.domain");

                return aliossUpload(file, type, chann, domain2);

            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }

    private Map<String, Object> aliossUpload(MultipartFile file, String type, String chann, String domain) {
        Map<String, Object> map = aliOSSClient.uploadFileStream(file, type, chann, domain);
        String status = String.valueOf(map.get("status"));
        if (status.equals("success")) {
            return map;
        } else {
            log.error("上传失败");
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传失败");
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
    public Map<String, Object> uploadFileObject(File file, String type, String chann) {

        String uploadMode = PropertiesLoader.getValue("upload.mode");
        //判断是否为允许的上传文件后缀
        boolean allowed = FileUtil.isAllowed(file.getName(), type);
        if (!allowed) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不允许的上传类型");
        }

        switch (uploadMode) {
            case "alioss":
                Map<String, Object> map = aliOSSClient.uploadLocalFile(file, type, chann);
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


    /**
     * 图片文件上传（上传本地图片用于帖子封面切割）
     *
     * @param file  file
     * @param type  img | doc | video
     * @return url | filename
     */
    public Map<String, Object> uploadMultipartFileObject(MultipartFile file, String type) {

        String uploadMode = PropertiesLoader.getValue("upload.mode");
        //判断是否为允许的上传文件后缀
        boolean allowed = FileUtil.isAllowed(file.getOriginalFilename(), type);
        if (!allowed) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不允许的上传类型");
        }

        switch (uploadMode) {
            case "alioss":
                Map map = uploadMultipartFile(file);
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

            default:
                log.error("上传模式不正确");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "上传模式不正确");
        }
    }

    /**
     * 图片上传到本地服务器
     *
     * @param file
     * @return
     */
    public Map uploadMultipartFile(MultipartFile file) {
        //获取文件名
        String filename = file.getOriginalFilename();
        //获取文件后缀
        String hz = file.getContentType();
        Map map = new HashMap();
        if (file.getSize() > 0) {
            try {
                String path = PropertiesLoader.getValue("post.incise.domain") + filename + hz;
                //SaveFileFromInputStream(file.getInputStream(), PropertiesLoader.getValue("post.incise.domain"), filename);
                map.put("status", "success");
                map.put("url", path);
                file.transferTo(new File(path));
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return map;
    }


    /**
     * 帖子封面切割并上传
     * @param file
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public Map uploadImgerAndIncision(String file, String x, String y, String w, String h) {

        BufferedInputStream is = null;
        ImageInputStream iis = null;
        Map map = new HashMap();

        try {
            URL u = new URL(file);
            //读取图片文件
            is = new BufferedInputStream(u.openStream());

            /**//*
             * 返回包含所有当前已注册 ImageReader 的 Iterator，这些 ImageReader
             * 声称能够解码指定格式。 参数：formatName - 包含非正式格式名称 .
             *（例如 "jpeg" 或 "tiff"）等 。
             */
            String suffix = file.substring(file.lastIndexOf(".") + 1);

            Iterator<ImageReader> it = ImageIO.getImageReadersByFormatName(suffix);
            ImageReader reader = it.next();
            //获取图片流
            iis = ImageIO.createImageInputStream(is);

            /**//*
             * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
             * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader
             *  避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
             */
            reader.setInput(iis, true);

            /**//*
             * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
             * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件
             * 将从其 ImageReader 实现的 getDefaultReadParam 方法中返回
             * ImageReadParam 的实例。
             */
            ImageReadParam param = reader.getDefaultReadParam();

            /**//*
             * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
             * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
             */
            Rectangle rect = null;
            if (StringUtil.isNotEmpty(x) && StringUtil.isNotEmpty(y) && StringUtil.isNotEmpty(w) && StringUtil.isNotEmpty(h)) {
                rect = new Rectangle(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(w), Integer.parseInt(h));
            }


            //提供一个 BufferedImage，将其用作解码像素数据的目标。
            param.setSourceRegion(rect);

            /**//*
             * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将
             * 它作为一个完整的 BufferedImage 返回。
             */
            BufferedImage bi = reader.read(0, param);
            UUID uuid = UUID.randomUUID();
            String incise = PropertiesLoader.getValue("post.incise.domain");
            incise += uuid + "." + suffix;
            //保存新图片
            ImageIO.write(bi, "jpg", new File(incise));
            //String domain = PropertiesLoader.getValue("formal.img.domain");

            map = aliOSSClient.uploadInciseStream(incise, "img", "coverIncise");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (iis != null)
                try {
                    iis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return map;
    }
}
