package com.movision.utils;

/**
 * 字符串工具类
 *
 * @Author zhuangyuhao
 * @Date 2017/3/30 21:25
 */
public class StrUtil {

    /**
     * 生成app用户的默认的昵称
     *
     * @param phone
     * @return mofo_180****9558
     */
    public static String genDefaultNickNameByPhone(String phone) {
        return "mofo_" + phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    public static void main(String[] args) {
        System.out.println(genDefaultNickNameByPhone("18051989558"));
    }

}
