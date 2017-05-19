package com.movision.utils.ali;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.SignatureMethod;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * @Author zhuangyuhao
 * @Date 2017/5/19 14:23
 */
public class AliUtils {

    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static final String ENCODE_TYPE = "UTF-8";

    /**
     * 生成符合规范的 Timestamp 字符串(适用于ali视频点播的API调用接口)
     *
     * @param date
     * @return
     */
    public static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /**
     * 生成规范化请求字符串（示例中的 canonicalizedQueryString 变量）
     *
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String percentEncode(String value) throws UnsupportedEncodingException {
        if (value == null) return null;
        return URLEncoder.encode(value, ENCODE_TYPE).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }


    public static void main(String[] args) {
        System.out.println(formatIso8601Date(new Date()));
    }
}
