package com.zhuhuibao.security;

import com.sun.crypto.provider.SunJCE;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

public class EncryptionForString {
    public static final String ENCODING_BASE64 = "BASE64";
    public static final String ENCODING_RAW = "RAW";
    public static final byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    static {
        Security.addProvider(new SunJCE());
    }

    public static byte[] encrypt(byte[] keyIV, String strIn, Key key, String strArithmetic, String encoding)
            throws Exception {
        Cipher cipher = Cipher.getInstance(strArithmetic + "/CBC/PKCS5Padding");

        IvParameterSpec ips = keyIV.length == 0 ? new IvParameterSpec(iv) : new IvParameterSpec(keyIV);
        cipher.init(1, key, ips);
        byte[] bOut = cipher.doFinal(strIn.getBytes("UTF-8"));
        if (encoding.equalsIgnoreCase("BASE64")) {
            bOut = EncodeUtil.encodeBase64(bOut);
        }

        return bOut;
    }

    public static byte[] encrypt(byte[] keyIV, String strIn, Key key, String encoding)
            throws Exception {
        return encrypt(keyIV, strIn, key, "desede", encoding);
    }

    public static byte[] decrypt(byte[] keyIV, byte[] bIn, byte[] seed, String strKeyArithmetic, String strArithmetic, String encoding)
            throws Exception {
        SecureRandom sr = new SecureRandom(seed);
        KeyGenerator kGen = KeyGenerator.getInstance(strKeyArithmetic);
        if (strArithmetic.equalsIgnoreCase("desede")) {
            kGen.init(168, sr);
        } else {
            kGen.init(sr);
        }
        Key key = kGen.generateKey();
        return decrypt(keyIV, bIn, key, strArithmetic, encoding);
    }

    public static byte[] decrypt(byte[] keyIV, byte[] bIn, Key key, String strArithmetic, String encoding)
            throws Exception {
        Cipher cipher = Cipher.getInstance(strArithmetic + "/CBC/PKCS5Padding");
        IvParameterSpec ips = keyIV.length == 0 ? new IvParameterSpec(iv) : new IvParameterSpec(keyIV);
        cipher.init(2, key, ips);
        byte[] bOut = cipher.doFinal(bIn);
        if (encoding.equalsIgnoreCase("BASE64")) {
            bOut = EncodeUtil.encodeBase64(bOut);
        }
        return bOut;
    }

    public static byte[] decrypt(byte[] keyIV, byte[] bIn, Key key)
            throws Exception {
        return decrypt(keyIV, bIn, key, "desede", "RAW");
    }

    public static byte[] decrypt(byte[] keyIV, byte[] bIn, String strKey)
            throws Exception {
        return decrypt(keyIV, bIn, strKey.getBytes("UTF-8"), "desede", "desede", "RAW");
    }

    static String bytesToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length);
        byte[] arrayOfByte = bytes;
        int j = bytes.length;
        for (int i = 0; i < j; i++) {
            byte b = arrayOfByte[i];
            sb.append((char) b);
        }
        return sb.toString();
    }

    static byte[] stringToBytes(String in) {
        byte[] bytes = new byte[in.length()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) in.charAt(i);
        }
        return bytes;
    }

    public static String signFreeUser(int seed, String asc) {
        String str = "";

        byte[] bytes = asc.getBytes();
        for (byte c : bytes) {
            String b = String.valueOf(c);
            str = str + b.length() + b;
        }

        int size = str.length();
        byte[] bytes2 = str.getBytes();
        byte[] bytes1 = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes1[i] = bytes2[(size - i - 1)];
        }
        str = new String(bytes1);

        String str1 = "";
        for (int i = 0; i < str.length(); i++) {
            int num = Integer.parseInt(str.substring(i, i + 1));
            String tmp = String.valueOf(num + seed);
            str1 = str1 + tmp.substring(tmp.length() - 1);
        }
        return str1;
    }
}

