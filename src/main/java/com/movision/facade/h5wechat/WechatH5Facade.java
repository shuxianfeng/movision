package com.movision.facade.h5wechat;

import com.movision.mybatis.post.service.PostService;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/8/18 15:48
 */
@Service
public class WechatH5Facade extends JPanel {
    private static Logger log = LoggerFactory.getLogger(WechatH5Facade.class);
    //下面是模板图片的路径
    String timgurl = PropertiesLoader.getValue("wechat.h5.domain");
    String newurl = PropertiesLoader.getValue("wechat.newh5.domain");//新图片路径
    String newurl2 = PropertiesLoader.getValue("wechat.h5.mofo");//新图片路径
    String headImg = PropertiesLoader.getValue("wechat.erweima.domain");//二维码路径
    String lihunurl = PropertiesLoader.getValue("wechat.lihun.domain");

    @Autowired
    private MovisionOssClient movisionOssClient;
    public Map<String, Object> imgCompose(String manname, String womanname, int type) {
        Map<String, Object> map = new HashMap<>();
//        public static void exportImg1(){
//            int width = 100;
//            int height = 100;
//            String s = "你好";
//
//            File file = new File("d:/image.jpg");
//
//            Font font = new Font("Serif", Font.BOLD, 10);
//            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g2 = (Graphics2D)bi.getGraphics();
//            g2.setBackground(Color.WHITE);
//            g2.clearRect(0, 0, width, height);
//            g2.setPaint(Color.RED);
//
//            FontRenderContext context = g2.getFontRenderContext();
//            Rectangle2D bounds = font.getStringBounds(s, context);
//            double x = (width - bounds.getWidth()) / 2;
//            double y = (height - bounds.getHeight()) / 2;
//            double ascent = -bounds.getY();
//            double baseY = y + ascent;
//
//            g2.drawString(s, (int)x, (int)baseY);
//
//            try {
//                ImageIO.write(bi, "jpg", file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        (String username,String headImg)
        if (type == 1) {//结婚证
            try {
                InputStream is = new FileInputStream(timgurl);
                map = He(is, manname, womanname);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ImageFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type == 2) {//离婚证
            try {
                InputStream is = new FileInputStream(lihunurl);
                map = He(is, manname, womanname);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ImageFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public Map He(InputStream is, String manname, String womanname) {
        Map map = new HashMap();
        try {

            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            //Graphics g = buffImg.getGraphics();
            Graphics2D g = (Graphics2D) buffImg.getGraphics();
            //创建你要附加的图象。//-----------------------------------------------这一段是将小图片合成到大图片上的代码
            //小图片的路径
            ImageIcon imgIcon = new ImageIcon(headImg);
            //得到Image对象。
            Image img = imgIcon.getImage();
            //将小图片绘到大图片上。
            //5,300 .表示你的小图片在大图片上的位置。
            //g.drawImage(img, 400, 15, null);

            g.fillRect(0, 0, getWidth(), getHeight());
            int drawX = (getWidth() - img.getWidth(this)) / 2;
            int drawY = (getHeight() - img.getHeight(this)) / 2;
            g.rotate(30, 900, -15);
            g.drawImage(img, drawX, drawY, this);
            //g.rotate(30);
            //设置颜色。
            g.setColor(Color.BLACK);

            //最后一个参数用来设置字体的大小
            Font f = new Font("宋体", Font.PLAIN, 25);
            //Color mycolor = Color.BLACK;//new Color(0, 0, 255);
            Color[] mycolor = {Color.ORANGE, Color.LIGHT_GRAY};
            // g.setColor(mycolor);
            g.setFont(f);
            //   平移原点到图形环境的中心
            g.translate(this.getWidth() / 2, this.getHeight() / 2);
            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            // g.drawString(manname, 160, 610);//合成男的名字new String(message.getBytes("utf8"),"gbk");
            //  g.drawString(womanname, 160, 720);//合成女的名字
            for (int i = 0; i < 1; i++) {
                g.rotate(30 * Math.PI / 180);
                g.setPaint(mycolor[i % 2]);
                g.drawString(manname, -160, 100);
                g.drawString(womanname, -160, 70);
            }
            g.dispose();

            //OutputStream os;

            //os = new FileOutputStream("d:/union.jpg");
            String shareFileName = System.currentTimeMillis() + ".jpg";

            map.put("status", 200);
            map.put("url", shareFileName);
            String url = newurl + shareFileName;
            //  os = new FileOutputStream(shareFileName);
            //创键编码器，用于编码内存中的图象数据。
            //JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            // en.encode(buffImg);
            ImageIO.write(buffImg, "png", new File(url));//图片的输出路径
            map.put("newurl", newurl2 + "/upload/wechat/" + shareFileName);
            is.close();
            //  os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ImageFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


}