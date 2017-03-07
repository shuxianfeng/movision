package com.movision.utils.im;

import com.movision.common.constant.ImConstant;

import java.security.MessageDigest;
import java.util.Date;

/**
 * 计算CheckSum
 *
 * @Author zhuangyuhao
 * @Date 2017/3/6 14:46
 */
public class CheckSumBuilder {

    // 计算并获取CheckSum

    /**
     * SHA1(AppSecret + Nonce + CurTime),三个参数拼接的字符串，进行SHA1哈希计算，转化成16进制字符(String，小写)
     *
     * @param appSecret
     * @param nonce
     * @param curTime
     * @return
     */
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return encode("md5", requestBody);
    }

    /**
     * 根据用户的手机号生成一个accid
     * @param phone
     * @return
     */
    public static String getAccid(String phone) {
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        return encode("md5", phone + ImConstant.APP_SECRET + curTime);
    }

    /**
     * 加密
     *
     * @param algorithm 加密方式
     * @param value     需要加密的字符串
     * @return
     */
    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static void main(String[] args) {
        System.out.println(getAccid("18051989558"));
    }

}
