package com.movision.utils;

import com.movision.utils.file.FileUtil;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/16 10:42
 */
public class ImgCompressUtil {

    private static final Logger log = LoggerFactory.getLogger(ImgCompressUtil.class);

    /**
     * 压缩图片：外部调用方法
     *
     * @param url      需要压缩的图片url，例如：F:/Download_pic/613.jpg
     * @param tempDir 压缩后的图片url，例如：E:/test/fileSource/mini613_2.jpg
     * @param w        宽
     * @param h        高
     * @return 压缩是否成功
     */
    public static boolean ImgCompress(String url, String tempDir, int w, int h) {
        // 压缩质量 百分比（无损压缩）
        float JPEGcompression = 1f;

        String name = FileUtil.getPicName(url);

        // 截取url中最后一个“/”之后的字符串为name
        log.info("url：===========" + url);
        log.info("name：=========" + name);

        // 压缩主方法
        return ImgCompress(tempDir, url, name, w, h, JPEGcompression);

    }

    /**
     * 图片压缩主方法
     *
     * @param tempDir        压缩后的图片的存放目录
     * @param url             需要压缩的图片的存放目录
     * @param name            图片名
     * @param w               目标宽
     * @param h               目标高
     * @param JPEGcompression 压缩质量/百分比
     * @return
     */
    public static boolean ImgCompress(String tempDir, String url, String name,
                                      int w, int h, float JPEGcompression) {
        boolean compressFlag = false;
        File file = new File(url);
        if (!(file.exists() && file.canRead())) {
            // 当文件不存在或者不能读取的时候
            // filePath = "/var/upload/404.jpg";
            // 需要记录图片的path
            log.info("图片不存在");
            return compressFlag;
        } else {
            try {
                //校验存储的文件夹是否存在，不存在时自动创建目录
                File tempfile = new File(tempDir);
                if (!tempfile.exists() && !tempfile.isDirectory()) {
                    tempfile.mkdir();
                }

                BufferedImage bufferedImage = ImageIO.read(file);

                File imgFile = new File(url);// 读入文件
                Image img = ImageIO.read(imgFile);      // 构造Image对象
                int originWidth = img.getWidth(null);    // 得到源图宽
                int originHeight = img.getHeight(null);  // 得到源图长


                Map<String, Integer> map = new HashMap<>();
                map.put("des_w", w);    //目标宽
                map.put("des_h", h);    //目标高
                map.put("w", originWidth);
                map.put("h", originHeight);

                //压缩宽高比处理逻辑 TODO
                map = resizeImgSize(w, h, map);
                int final_w = map.get("w");    //最终的宽度
                int final_h = map.get("h");    //最终的高度

                BufferedImage image_to_save;//------------------------------解决压缩后图片变红的问题20170411 13:46 shuxf
                if (bufferedImage.isAlphaPremultiplied()) {
                    image_to_save = new BufferedImage(final_w, final_h, BufferedImage.TRANSLUCENT);
                } else {
                    image_to_save = new BufferedImage(final_w, final_h, BufferedImage.TYPE_INT_RGB);
                }

                image_to_save.getGraphics().drawImage(
                        bufferedImage.getScaledInstance(final_w, final_h,
                                Image.SCALE_SMOOTH), 0, 0, null);
                log.info("第二次测试压缩核心方法中压缩后的图片存储路径>>>>>>>>>>>>>>>>" + tempDir + name);
                FileOutputStream fos = new FileOutputStream(tempDir + name); // 输出到文件流

                // 新的方法
                int dpi = 300;// 分辨率
                saveAsJPEG(dpi, image_to_save, JPEGcompression, fos);
                // 关闭输出流
                fos.close();
                compressFlag = true;
            } catch (IOException ex) {
                log.error("压缩图片异常 原图路径" + url, ex);
            }
        }

        return compressFlag;

    }

    /**
     * 重新设置图片尺寸（核心算法）
     * <p>
     * 正常情况下压缩一次：
     * 第一次压缩逻辑：	（1）若图片原始 宽高比 > w/h, 则以宽度为基准，等比例放缩图片
     * （2）若图片原始 宽高比 < w/h, 则以高度为基准，等比例缩放图片
     * <p>
     * 特殊情况：
     * 第一次压缩后，若发现 宽>w,则再次进行压缩；同理，若 高 > h,则再次进行压缩
     * 第二次的压缩逻辑：	(1)若第一次压缩后的宽度大于的设定的宽度，则以设定的宽度为基准，缩小高度
     * (2)若第一次压缩后的高度大于设定的高度，则已设定的高度为基准，缩小宽度
     *
     * @param w   设定好的宽
     * @param h   设定好的高
     * @param map 当前的尺寸map
     */
    public static Map<String, Integer> resizeImgSize(int w, int h, Map<String, Integer> map) {
        int width = map.get("w");
        int height = map.get("h");

        if (width < w && height < h) {
            return map;
        }


        if (width / height > w / h) {
            //以宽度为基准，等比例放缩图片
            map.put("h", (int) (height * w / width));
            map.put("w", w);
        } else {
            //以高度为基准，等比例缩放图片
            map.put("h", h);
            map.put("w", (int) (width * h / height));
        }

        int new_w = map.get("w");
        int new_h = map.get("h");
        // System.out.println("第一次压缩后的宽度，w="+new_w);
        // System.out.println("第一次压缩后的高度，h="+new_h);

        if (new_w > w && new_h == h) {
            //若第一次压缩后的宽度大于的设定的宽度，则以设定的宽度为基准，缩小高度
            map.put("w", w);
            map.put("h", (int) w * new_h / new_w);
        }
        if (new_h > h && new_w == w) {
            //若第一次压缩后的高度大于设定的高度，则已设定的高度为基准，缩小宽度
            map.put("h", h);
            map.put("w", (int) h * new_w / new_h);
        }

        return map;
    }

    /**
     * 以JPEG编码保存图片
     *
     * @param dpi             分辨率
     * @param image_to_save   要处理的图像图片
     * @param JPEGcompression 压缩比
     * @param fos             文件输出流
     * @throws IOException
     */
    public static void saveAsJPEG(Integer dpi, BufferedImage image_to_save,
                                  float JPEGcompression, FileOutputStream fos) throws IOException {

        // Image writer
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO
                .getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                new ImageTypeSpecifier(image_to_save), null);

        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
                    .getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        imageWriter.write(imageMetaData,
                new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

    }
}
