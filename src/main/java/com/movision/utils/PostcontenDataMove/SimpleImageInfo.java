package com.movision.utils.PostcontenDataMove;

import com.lowagie.text.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    private int height;
    private int width;
    private String mimeType;

    public SimpleImageInfo(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        try {
            processStream(is);
        } finally {
            is.close();
        }
    }

    public SimpleImageInfo(InputStream is) throws IOException {
        processStream(is);
    }

    public SimpleImageInfo(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        try {
            processStream(is);
        } finally {
            is.close();
        }
    }

    private void processStream(InputStream is) throws IOException {
        int c1 = is.read();
        int c2 = is.read();
        int c3 = is.read();

        mimeType = null;
        width = height = -1;

        if (c1 == 'G' && c2 == 'I' && c3 == 'F') { // GIF
            is.skip(3);
            width = readInt(is, 2, false);
            height = readInt(is, 2, false);
            mimeType = "image/gif";
        } else if (c1 == 0xFF && c2 == 0xD8) { // JPG
            while (c3 == 255) {
                int marker = is.read();
                int len = readInt(is, 2, true);
                if (marker == 192 || marker == 193 || marker == 194) {
                    is.skip(1);
                    height = readInt(is, 2, true);
                    width = readInt(is, 2, true);
                    mimeType = "image/jpeg";
                    break;
                }
                is.skip(len - 2);
                c3 = is.read();
            }
        } else if (c1 == 137 && c2 == 80 && c3 == 78) { // PNG
            is.skip(15);
            width = readInt(is, 2, true);
            is.skip(2);
            height = readInt(is, 2, true);
            mimeType = "image/png";
        } else if (c1 == 66 && c2 == 77) { // BMP
            is.skip(15);
            width = readInt(is, 2, false);
            is.skip(2);
            height = readInt(is, 2, false);
            mimeType = "image/bmp";
        } else {
            int c4 = is.read();
            if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
                    || (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) { //TIFF
                boolean bigEndian = c1 == 'M';
                int ifd = 0;
                int entries;
                ifd = readInt(is, 4, bigEndian);
                is.skip(ifd - 8);
                entries = readInt(is, 2, bigEndian);
                for (int i = 1; i <= entries; i++) {
                    int tag = readInt(is, 2, bigEndian);
                    int fieldType = readInt(is, 2, bigEndian);
                    int valOffset;
                    if ((fieldType == 3 || fieldType == 8)) {
                        valOffset = readInt(is, 2, bigEndian);
                        is.skip(2);
                    } else {
                        valOffset = readInt(is, 4, bigEndian);
                    }
                    if (tag == 256) {
                        width = valOffset;
                    } else if (tag == 257) {
                        height = valOffset;
                    }
                    if (width != -1 && height != -1) {
                        mimeType = "image/tiff";
                        break;
                    }
                }
            }
        }
        if (mimeType == null) {
            throw new IOException("Unsupported image type");
        }
    }

    private int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
        int ret = 0;
        int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
        int cnt = bigEndian ? -8 : 8;
        for (int i = 0; i < noOfBytes; i++) {
            ret |= is.read() << sv;
            sv += cnt;
        }
        return ret;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "MIME Type : " + mimeType + "\t Width : " + width
                + "\t Height : " + height;
    }

    public static SimpleImageInfo getByFileUrl(String url) throws IOException {
        File file = new File(url);
        log.debug(file.getName());
        SimpleImageInfo simpleImageInfo = new SimpleImageInfo(file);
        log.debug("宽：" + simpleImageInfo.getWidth());
        log.debug("高：" + simpleImageInfo.getHeight());
        return simpleImageInfo;
    }

    public static Map getWhBYOnlineUrl(String url) throws Exception {
        Image img = Image.getInstance(new URL(url));
        System.out.println("img.width=" + img.width() + " img.hight=" + img.height());
        Map map = new HashMap<>();
        map.put("w", (int) img.width());
        map.put("h", (int) img.height());
        System.out.println(map.toString());
        return map;
    }

    public static String getWH(String url) throws Exception {

        Map map = getWhBYOnlineUrl(url);

        String w = String.valueOf(map.get("w"));
        String h = String.valueOf(map.get("h"));
        String wh = w + "x" + h;
        log.debug("wh:" + wh);
        return wh;
    }


    public static void main(String[] args) throws IOException {
        /*try {
            File f = new File("C:\\Users\\Administrator\\Downloads\\美女.jpg"); //C:\Users\Administrator\Downloads
            // Getting image data from a InputStream
            FileInputStream b = new FileInputStream(f);
            SimpleImageInfo imageInfo = new SimpleImageInfo(b);
            System.out.println(imageInfo);
            // Getting image data from a file
            imageInfo = new SimpleImageInfo(f);
            System.out.println(imageInfo);
            // Getting image data from a byte array
            byte[] buffer = new byte[1024 * 6];
            InputStream is = new FileInputStream(f);
            while (is.read(buffer) == -1) {}
            is.close();
            imageInfo = new SimpleImageInfo(buffer);
            System.out.println(imageInfo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

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
