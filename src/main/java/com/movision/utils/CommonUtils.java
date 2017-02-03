package com.movision.utils;

import java.math.BigDecimal;
import java.util.List;

public class CommonUtils {

    /**
     * 转换成骆驼峰格式名称
     *
     * @param str string
     * @return
     */
    public static String getCamelString(String str) {

        if (str == null || str.trim().length() == 0) {
            return str;
        }
        StringBuilder sb = new StringBuilder("");
        String[] strArray = str.split("_");
        if (strArray.length > 0) {
            sb.append(strArray[0]);
            for (int i = 1; i < strArray.length; i++) {
                sb.append(strArray[i].substring(0, 1).toUpperCase()).append(
                        strArray[i].substring(1));
            }
        }

        return sb.toString();
    }

    /**
     * 使用指定连接符拼接字符串
     *
     * @param list      需要拼接的字符串集合
     * @param connectorStr 连接符
     * @return string
     */
    public static String splice(List<String> list, String connectorStr) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str).append(connectorStr);
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    /**
     * 计算一个连接符出现在字符串中的次数
     * @param str           需要计算的字符串
     * @param connectorStr  连接符
     * @return
     */
    public static int getCountAppearInString(String str,String connectorStr){
        int count = 0;
        int start = 0;
        while (str.indexOf(connectorStr, start) >= 0 && start < str.length()) {
            count++;
            start = str.indexOf(connectorStr, start) + connectorStr.length();
        }
        return count;
    }


    /**
     *
     * @param bytes
     * @return
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }


    /**
     * 把字符串形式的id转换为int数组形式
     *
     * @param ids
     * @return
     */
    public static int[] idsStringToIntArray(String ids) {
        String[] arr = ids.split(",");
        int[] useridArray = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            useridArray[i] = Integer.valueOf(arr[i]);
        }
        return useridArray;
    }


    public static void main(String[] args) {
//        List<String> list = new ArrayList<>();
//        list.add("aaa");
//        list.add("bbb");
//        list.add("ccc");
//        String s = splice(list,"#");
//        System.out.println(s);
//       System.out.println( getCountAppearInString("aaa#bbb#ccc","#"));
       String s = CommonUtils.bytes2kb(323);
        System.out.println(s);
    }
}
