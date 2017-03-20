package com.movision.fsearch.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtil {

    private static final String MD5 = "MD5";

    public static String md5(String s) {
        return encrypt(s, MD5);
    }

    public static String encrypt(String s, String algorithm) {

        byte[] unencodedPassword = s.getBytes();

        MessageDigest md = null;

        // first create an instance, given the provider
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] bytes = md.digest();

        StringBuffer result = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                result.append("0");
            }

            result.append(Long.toString((int) bytes[i] & 0xff, 16));
        }

        return result.toString();
    }

    public static String genSalt(int length) {// throws
        // UnsupportedEncodingException
        // byte[] salt=new byte[12];
        // new Random().nextBytes(salt);
        // return new String(salt);
        if (length < 1) {
            throw new IllegalArgumentException("salt length < 1");
        }
        StringBuilder salt = new StringBuilder();
        for (int i = 0; i < length; i++) {
            salt.append((char) (33 + (int) (Math.random() * (126 - 33 + 1))));
        }
        return salt.toString();
    }

    public static String encryptPassword(String salt, String password) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        m.update(salt.getBytes());
        m.update(password.getBytes());// UTF8
        byte[] bytes = m.digest();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            result.append(Integer.toHexString(
                    (0x000000ff & bytes[i]) | 0xffffff00).substring(6));
        }

        // for (int i = 0; i < bytes.length; i++) {
        // if (((int) bytes[i] & 0xff) < 0x10) {
        // result.append("0");
        // }
        // result.append(Long.toString((int) bytes[i] & 0xff, 16));
        // }

        return result.toString();
    }

}
