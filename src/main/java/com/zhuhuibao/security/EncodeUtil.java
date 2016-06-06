package com.zhuhuibao.security;

import org.apache.shiro.crypto.hash.Md5Hash;

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
    	System.out.println(encodeBase64ToString("yu1234".getBytes()));
    	System.out.println(encodeBase64ToString("1qaz2wsx".getBytes()));
    	System.out.println(encodeBase64ToString("123456".getBytes()));
    	System.out.println(encodeBase64ToString("caijl@456".getBytes()));
//		System.out.println(decodeBase64ToString(encodeBase64ToString("123".getBytes())));
//        System.out.println(new Md5Hash("123456a",null,2).toString());
	}
}

