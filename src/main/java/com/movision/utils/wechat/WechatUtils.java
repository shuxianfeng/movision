package com.movision.utils.wechat;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * @Author shuxf
 * @Date 2018/2/2 14:37
 * 微信小程序后台开发过程中的所有静态工具类
 */
public class WechatUtils {

    /**
     * 用于生成微信小程序需要的3rd_session的标准字符串(真随机数)
     * @return
     */
    public static String get3rdstr(){

        //SecureRandom是java中生成真随机数的类，此处不推荐用Random
        SecureRandom  ran = new SecureRandom();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < 128; i++) {
            int r = ran.nextInt(2);//随机整数的范围0~1，必须<2
            hexString.append(r);
        }
        System.out.println(hexString.toString());
        return hexString.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        WechatUtils.get3rdstr();
    }
}
