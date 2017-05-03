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

    public static String genDefaultNicknameByQQ(String str) {
        return "mofo_" + str.substring(0, str.length() - (str.substring(3)).length()) + "***" + str.substring(5);
    }

    public static String genDefaultNickNameForOpenid() {
        return "mofo_" + System.currentTimeMillis();
    }

    public static String genNickNameByDevice() {
        return "mofo_" + System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(genDefaultNickNameForOpenid());
    }

}
