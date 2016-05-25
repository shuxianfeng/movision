package com.zhuhuibao.alipay.util;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Properties;

import com.zhuhuibao.security.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PayVerify {

    private static final Logger logger = LoggerFactory
            .getLogger(PayVerify.class);

    private static Properties properties = null;

    static {
        try {
            properties = PropertiesLoaderUtils.loadAllProperties("web/alipay.properties");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("加载pay.properties属性文件出错：" + e.getMessage());
        }
    }

    public static String getPropertyValue(String key) {
        return properties.getProperty(key);
    }


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key,
                                 String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        return mysign.equals(sign);
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"
                    + charset);
        }
    }

}
