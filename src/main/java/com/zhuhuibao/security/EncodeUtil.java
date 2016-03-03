package com.zhuhuibao.security;

import java.io.UnsupportedEncodingException;

public class EncodeUtil {
    public static byte[] decodeBase64(String strIn) {
        return Base64.decode(strIn);
    }
    
    public static String decodeBase64ToString(String strIn) throws UnsupportedEncodingException {
        return new String(Base64.decode(strIn),"UTF-8");
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

    public static String encodeBase64ToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(encodeBase64(bytes),"UTF-8");
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
    	//123456 MTIzNDU2  123 MTIz  19630759@qq.com MTk2MzA3NTlAcXEuY29t
    	System.out.println(encodeBase64ToString("http://139.196.189.100/rest/validateMail?vm=dmFsaWRhdGUseXhkX3podWh1aThAc29odS5jb20sMjAxNi0wMy0wNCAwOTo1ODo0Mw==".getBytes()));
		System.out.println(decodeBase64ToString(encodeBase64ToString("MTIzNDU2".getBytes())));
	}
}

