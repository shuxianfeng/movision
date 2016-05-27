package com.zhuhuibao.utils;

public class CommonUtils {

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
}
