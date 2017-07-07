package com.movision.utils.PostcontenDataMove;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/6/19 17:20
 */
public class ReturnImgWH {

    private static Logger log = LoggerFactory.getLogger(ReturnImgWH.class);

    //读取远程url图片,得到宽高
    public static int[] returnImgWH(String imgurl) {
        boolean b = false;
        try {
            //实例化url
            URL url = new URL(imgurl);
            //载入图片到输入流
            java.io.BufferedInputStream bis = new BufferedInputStream(url.openStream());
            //实例化存储字节数组
            byte[] bytes = new byte[1024 * 1024];
            //设置写入路径以及图片名称
            OutputStream bos = new FileOutputStream(new File("E:\\img_temp/thetempimg.jpg"));
            int len;
            while ((len = bis.read(bytes)) > 0) {
                bos.write(bytes, 0, len);
            }
            bis.close();
            bos.flush();
            bos.close();
            //关闭输出流
            b = true;
        } catch (Exception e) {
            //如果图片未找到
            b = false;
        }
        int[] a = new int[2];
        if (b) {//图片存在
            //得到文件
            java.io.File file = new java.io.File("E:\\img_temp/thetempimg.jpg");
            BufferedImage bi = null;
            boolean imgwrong = false;
            try {
                //读取图片
                bi = javax.imageio.ImageIO.read(file);
                try {
                    //判断文件图片是否能正常显示,有些图片编码不正确
                    int i = bi.getType();
                    imgwrong = true;
                } catch (Exception e) {
                    imgwrong = false;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (imgwrong) {
                a[0] = bi.getWidth(); //获得 宽度
                a[1] = bi.getHeight(); //获得 高度
            } else {
                a = null;
            }
            //删除文件
            file.delete();
        } else {//图片不存在
            a = null;
        }
        return a;

    }

    public static String getWH(String url) {
        int[] a = returnImgWH(url);
        int w = 0, h = 0;
        if (null == a) {
            log.error("图片未找到，url:" + url);
        } else {
            w = a[0];
            h = a[1];
            log.debug("w:" + w);
            log.debug("h:" + h);

        }
        return w + "x" + h;
    }


    public static void main(String[] args) {
//        ReturnImgWH i = new ReturnImgWH();
//        int[] a = returnImgWH("http://pic.mofo.shop/upload/activity/img/aMp906YA1496897737441.jpg");
//        if (a == null) {
//            System.out.println("图片未找到!");
//        } else {
//            System.out.println("宽为" + a[0]);
//            System.out.println("高为" + a[1]);
//        }
        log.debug(getWH("http://pic.mofo.shop/upload/activity/img/aMp906YA1496897737441.jpg"));
    }
}
