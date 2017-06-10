package com.movision.utils;

import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author zhurui
 * @Date 2017/6/10 13:15
 */
@Service
public class CoverImgCompressUtil {

    private static Logger log = LoggerFactory.getLogger(CoverImgCompressUtil.class);

    @Autowired
    private MovisionOssClient movisionOssClient;

    public String ImgCompress(MultipartFile file) {
        int w = 750;//图片压缩后的宽度
        int h = 440;//图片压缩后的高度440
        Boolean compressFlag = false;
        String compress_dir_path = PropertiesLoader.getValue("post.incise.domain");//压缩图片路径url
        List<String> existFileList = getExistFiles(compress_dir_path);//获取文件夹下的所有文件名

        //上传到本地服务器
        Map m = movisionOssClient.uploadMultipartFileObject(file, "img");

        String PATH = String.valueOf(m.get("url"));

        String filename = file.getOriginalFilename();
        //获取图片名
        String tempfilename = UUID.randomUUID() + file.getContentType();


        // 1 生成压缩后的图片的url
        String compress_file_path = compress_dir_path;
        log.info("压缩后的图片url，compress_file_path=" + compress_file_path);

        // 2 判断该文件夹下是否有同名的图片，若有则不处理，若没有则进行处理
        if (CollectionUtils.isEmpty(existFileList) || !existFileList.contains(filename)) {
            // 压缩核心算法
            compressFlag = compressJpgOrPng(w, h, compressFlag, filename, PATH, compress_file_path);
            // 处理过的图片加入到已处理集合，防止重复压缩图片
            existFileList.add(filename);
        } else {
            compressFlag = true;
            log.info("该图片已存在，不需要压缩，filename=" + filename);
        }
        if (compressFlag) {
            //压缩成功后返回图片压缩后的url
            return compress_file_path + filename;
        }

        return null;
    }

    public String ImgCompress(String file) {
        int w = 750;//图片压缩后的宽度
        int h = 440;//图片压缩后的高度440
        Boolean compressFlag = false;
        String compress_dir_path = PropertiesLoader.getValue("post.incise.domain");//压缩图片路径url
        String compress_dir_local_path = PropertiesLoader.getValue("post.img.local.domain");//获取项目根目录/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision
        List<String> existFileList = getExistFiles(compress_dir_path);//获取文件夹下的所有文件名

        File f = new File(file);
        Long size = f.length();//获取文件大小

        if (size < 200) {
            String PATH = file;
            //获取文件名
            String tempDir = file.substring(0, file.lastIndexOf("/")) + compress_dir_local_path;
            String filename = FileUtil.getPicName(file);//获取图片文件名
            // 1 生成压缩后的图片的url
            String tempfilename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."), filename.length() - 1);
            String compress_file_path = compress_dir_path + tempfilename;
            log.info("压缩后的图片url，compress_file_path=" + compress_file_path);

            // 2 判断该文件夹下是否有同名的图片，若有则不处理，若没有则进行处理
            if (CollectionUtils.isEmpty(existFileList) || !existFileList.contains(filename)) {
                // 压缩核心算法
                compressFlag = compressJpgOrPng(w, h, compressFlag, filename, PATH, tempDir);
                // 处理过的图片加入到已处理集合，防止重复压缩图片
                existFileList.add(filename);
            } else {
                compressFlag = true;
                log.info("该图片已存在，不需要压缩，filename=" + filename);
            }
            if (compressFlag) {
                //压缩成功后返回图片压缩后的url
                return compress_file_path;
            }
        } else {

        }

        return null;
    }


    public boolean compressJpgOrPng(int w, int h, boolean compressFlag, String filename, String filePath, String tempDir) {
        if (filename.endsWith(".jpg")) {

            log.info("压缩jpg图片，filepath=" + filePath);
            compressFlag = ImgCompressUtil.ImgCompress(filePath, tempDir, w, h);
        }
        if (filename.endsWith(".png")) {
            log.info("压缩png图片，filepath=" + filePath);
            compressFlag = ImgCompressUtil.ImgCompress(filePath, tempDir, w, h);
        }
        if (filename.endsWith(".jpeg")) {

            log.info("压缩jpeg图片，filepath=" + filePath);
            compressFlag = ImgCompressUtil.ImgCompress(filePath, tempDir, w, h);
        }
        if (filename.endsWith(".bmp")) {

            log.info("压缩bmp图片，filepath=" + filePath);
            compressFlag = ImgCompressUtil.ImgCompress(filePath, tempDir, w, h);
        }
        return compressFlag;
    }


    /**
     * 获取指定文件夹下的文件 不分类型，即png,jpg都有
     *
     * @param compress_dir_path
     * @return
     */
    public List<String> getExistFiles(String compress_dir_path) {
        List<String> existFileList = new ArrayList<>();
        try {
            existFileList = FileUtil.readfileName(compress_dir_path, null);
            log.info("指定路径下的图片有：" + existFileList.toArray().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existFileList;
    }


    private static Image img;
    private static int width;
    private static int height;

//==========================================================================================================================================

    /**
     * 构造函数
     */
    public String ImgCompressto(String fileName) {
        try {
            File file = new File(fileName);// 读入文件
            img = ImageIO.read(file);      // 构造Image对象
            width = img.getWidth(null);    // 得到源图宽
            height = img.getHeight(null);  // 得到源图长
            int w = 750;//图片压缩后的宽度
            int h = 440;//图片压缩后的高度440
            String type = fileName.substring(fileName.lastIndexOf(".") + 1);
            return resize(w, h, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造函数
     */
    public String ImgCompressto(MultipartFile fileName) {
        try {
            BufferedImage image = ImageIO.read(fileName.getInputStream());
            width = image.getWidth();    // 得到源图宽
            height = image.getHeight();  // 得到源图长
            int w = 750;//图片压缩后的宽度
            int h = 440;//图片压缩后的高度440
            String tye = fileName.getOriginalFilename();
            return resizeFix(w, h, tye);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照宽度还是高度进行压缩
     *
     * @param w int 最大宽度
     * @param h int 最大高度
     */
    public String resizeFix(int w, int h, String tye) throws IOException {
        if (width / height > w / h) {
            return resizeByWidth(w, tye);
        } else {
            return resizeByHeight(h, tye);
        }
    }

    /**
     * 以宽度为基准，等比例放缩图片
     *
     * @param w int 新宽度
     */
    public String resizeByWidth(int w, String type) throws IOException {
        int h = (int) (height * w / width);
        return resize(w, h, type);
    }

    /**
     * 以高度为基准，等比例缩放图片
     *
     * @param h int 新高度
     */
    public String resizeByHeight(int h, String type) throws IOException {
        int w = (int) (width * h / height);
        return resize(w, h, type);
    }

    /**
     * 强制压缩/放大图片到固定的大小
     *
     * @param w int 新宽度
     * @param h int 新高度
     */
    public String resize(int w, int h, String type) throws IOException {
        // SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图
        String path = PropertiesLoader.getValue("post.incise.domain") + UUID.randomUUID() + type;
        File destFile = new File(path);
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
        // 可以正常实现bmp、png、gif转jpg
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        encoder.encode(image); // JPEG编码
        out.close();
        return null;
    }


}
