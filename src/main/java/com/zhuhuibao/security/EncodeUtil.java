package com.zhuhuibao.security;

public class EncodeUtil {
    public static byte[] decodeBase64(String strIn) {
        return Base64.decode(strIn);
    }

    public static byte[] decodeBase64(byte[] bytes) {
        return Base64.decode(bytes);
    }

    public static byte[] encodeBase64(byte[] bytes) {
        return Base64.encode(bytes);
    }

    public static byte[] encodeBase64(String str) {
        return encodeBase64(str.getBytes());
    }

    public static String encodeBase64ToString(byte[] bytes) {
        return new String(encodeBase64(bytes));
    }
}

