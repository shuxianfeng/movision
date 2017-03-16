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
     * @param filePath 压缩后的图片url，例如：E:/test/fileSource/mini613_2.jpg
     * @param w        宽
     * @param h        高
     * @return 压缩是否成功
     */
    public static boolean ImgCompress(String url, String filePath, int w, int h) {
        // 压缩质量 百分比
        float JPEGcompression = 0.7f;

        String name = FileUtil.getPicName(url);

        // 截取url中最后一个“/”之后的字符串为name
        log.info("url：===========" + url);
        log.info("name：=========" + name);

        // 压缩主方法
        return ImgCompress(filePath, url, name, w, h, JPEGcompression);

    }

    /**
     * 图片压缩主方法
     *
     * @param filePath        压缩后的图片的url
     * @param url             需要压缩的图片的url
     * @param name            图片名
     * @param w               目标宽
     * @param h               目标高
     * @param JPEGcompression 压缩质量/百分比
     * @return
     */
    public static boolean ImgCompress(String filePath, String url, String name,
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
                BufferedImage bufferedImage = ImageIO.read(file);

				/*//获取Img
                Image src = Toolkit.getDefaultToolkit().createImage(url);

				// 注释掉的部分是将压缩后的图像调整为方形
				int old_w = bufferedImage.getWidth(null); // 得到源图宽
				int old_h = bufferedImage.getHeight(null);// 得到源图高
				int new_w = 0;
				int new_h = 0;
				double w2 = (old_w * 1.00) / (w * 1.00);
				double h2 = (old_h * 1.00) / (h * 1.00);
				// 图片跟据长宽留白，成一个正方形图。
				BufferedImage oldpic;
				if (old_w > old_h) {
					oldpic = new BufferedImage(old_w, old_w,
							BufferedImage.TYPE_INT_RGB);
				} else {
					if (old_w < old_h) {
						oldpic = new BufferedImage(old_h, old_h,
								BufferedImage.TYPE_INT_RGB);
					} else {
						oldpic = new BufferedImage(old_w, old_h,
								BufferedImage.TYPE_INT_RGB);
					}
				}
				Graphics2D g = oldpic.createGraphics();
				g.setColor(Color.white);
				if (old_w > old_h) {
					g.fillRect(0, 0, old_w, old_w);

					g.drawImage(src, 0, (old_w - old_h) / 2, old_w, old_h,
							Color.white, null);
				} else {
					if (old_w < old_h) {
						g.fillRect(0, 0, old_h, old_h);
						g.drawImage(src, (old_h - old_w) / 2, 0, old_w, old_h,
								Color.white, null);
					} else {
						// g.fillRect(0,0,old_h,old_h);
						g.drawImage(src.getScaledInstance(old_w, old_h,
								Image.SCALE_SMOOTH), 0, 0, null);
					}
				}
				g.dispose();
				src = oldpic;
				// 图片调整为方形结束
				if (old_w > w)
					new_w = (int) Math.round(old_w / w2);
				else
					new_w = old_w;
				if (old_h > h)
					new_h = (int) Math.round(old_h / h2);// 计算新图长宽
				else
					new_h = old_h;*/

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

                // System.out.println("final_w="+final_w);
                // System.out.println("final_h="+final_h);

                BufferedImage image_to_save = new BufferedImage(final_w, final_h,
                        bufferedImage.getType());
                image_to_save.getGraphics().drawImage(
                        bufferedImage.getScaledInstance(final_w, final_h,
                                Image.SCALE_SMOOTH), 0, 0, null);
                FileOutputStream fos = new FileOutputStream(filePath); // 输出到文件流

                // 旧的使用 jpeg classes进行处理的方法
                // JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
                // JPEGEncodeParam jep =
                // JPEGCodec.getDefaultJPEGEncodeParam(image_to_save);
				/* 压缩质量 */
                // jep.setQuality(per, true);
                // encoder.encode(image_to_save, jep);

                // 新的方法
                int dpi = 300;// 分辨率
                saveAsJPEG(dpi, image_to_save, JPEGcompression, fos);
                // 关闭输出流
                fos.close();
                compressFlag = true;
            } catch (IOException ex) {
                log.error("压缩图片异常 原图路径" + url, ex);
                // filePath = "/var/upload/404.jpg";
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

        // useful documentation at
        // http://docs.oracle.com/javase/7/docs/api/javax/imageio/metadata/doc-files/jpeg_metadata.html
        // useful example program at
        // http://johnbokma.com/java/obtaining-image-metadata.html to output
        // JPEG data

        // old jpeg class
        // com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder =
        // com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(fos);
        // com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam =
        // jpegEncoder.getDefaultJPEGEncodeParam(image_to_save);

        // Image writer
        JPEGImageWriter imageWriter = (JPEGImageWriter) ImageIO
                .getImageWritersBySuffix("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
        imageWriter.setOutput(ios);
        // and metadata
        IIOMetadata imageMetaData = imageWriter.getDefaultImageMetadata(
                new ImageTypeSpecifier(image_to_save), null);

        // if(dpi != null && !dpi.equals("")){
        //
        // //old metadata
        // //jpegEncodeParam.setDensityUnit(com.sun.image.codec.jpeg.JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
        // //jpegEncodeParam.setXDensity(dpi);
        // //jpegEncodeParam.setYDensity(dpi);
        //
        // //new metadata
        // Element tree = (Element)
        // imageMetaData.getAsTree("javax_imageio_jpeg_image_1.0");
        // Element jfif =
        // (Element)tree.getElementsByTagName("app0JFIF").item(0);
        // jfif.setAttribute("Xdensity", Integer.toString(dpi) );
        // jfif.setAttribute("Ydensity", Integer.toString(dpi));
        //
        // }

        if (JPEGcompression >= 0 && JPEGcompression <= 1f) {

            // old compression
            // jpegEncodeParam.setQuality(JPEGcompression,false);

            // new Compression
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter
                    .getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(JPEGcompression);

        }

        // old write and clean
        // jpegEncoder.encode(image_to_save, jpegEncodeParam);

        // new Write and clean up
        imageWriter.write(imageMetaData,
                new IIOImage(image_to_save, null, null), null);
        ios.close();
        imageWriter.dispose();

    }
}
