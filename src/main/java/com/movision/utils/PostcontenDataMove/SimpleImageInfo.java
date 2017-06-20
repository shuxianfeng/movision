package com.movision.utils.PostcontenDataMove;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 不需要加载整个图片，从而节省内存空间。
 * <p>
 * 一般的方法：
 * BufferedImage bimg = ImageIO.read(new File("d:\\image\\org.jpg"));
 * int width = bimg.getWidth();
 * int height = bimg.getHeight();
 * 但这方法将整张图片都加载到了内存，如果那些图片比较大，无疑将增加服务的压力。
 *
 * @Author zhuangyuhao
 * @Date 2017/6/16 13:34
 */
public class SimpleImageInfo {

    private static Logger log = LoggerFactory.getLogger(SimpleImageInfo.class);

    public static String getWH(String url) throws Exception {
        String wh = "";
        BufferedImage image = getBufferedImage(url);
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            wh = width + "x" + height;
            log.debug("wh:" + wh);
        } else {
            log.error("图片不存在！");
        }

        return wh;
    }

    /**
     * @param imgUrl 图片地址
     * @return
     */
    public static BufferedImage getBufferedImage(String imgUrl) {
        URL url = null;
        InputStream is = null;
        BufferedImage img = null;
        try {
            url = new URL(imgUrl);
            is = url.openStream();
            img = ImageIO.read(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }


    public static void main(String[] args) throws IOException {
        try {
//            Map map = getWhBYOnlineUrl("http://pic.mofo.shop/upload/post/img/5f0K3m491496883825116.jpg");
            String url = "http://pic.mofo.shop/upload/activity/img/aMp906YA1496897737441.jpg";
            getWH(url);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }


    }


}
