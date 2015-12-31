package com.zhuhuibao.security;

import java.security.MessageDigest;

public class DigestForString {
    public static final String ENCODING_BASE64 = "BASE64";
    public static final String ENCODING_RAW = "RAW";

    public static String message(String strInput, String strArithmetic, String encoding)
            throws Exception {
        if ((strArithmetic == null) || (strArithmetic.equals("")) ||
                (strInput == null)) {
            throw new Exception("must have message content and arithmetic!\n");
        }

        if ((encoding == null) || (encoding.equals(""))) {
            encoding = "RAW";
        }

        String strOut = "";
        byte[] bOut = null;
        byte[] bIn = strInput.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance(strArithmetic);

        md.update(bIn);
        bOut = md.digest();

        if (encoding.equalsIgnoreCase("BASE64")) {
            bOut = EncodeUtil.encodeBase64(bOut);
        }

        strOut = new String(bOut);

        return strOut;
    }

    public static byte[] messageSHA1(String strInput)
            throws Exception {
        byte[] bOut = null;
        byte[] bIn = strInput.getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("SHA-1");

        md.update(bIn);
        bOut = md.digest();

        return bOut;
    }

    public static String message(String strInput, String encoding)
            throws Exception {
        return message(strInput, "SHA-1", encoding);
    }

    public static String message(String strInput) throws Exception {
        return message(strInput, "SHA-1", "BASE64");
    }

    public static boolean verify(byte[] newMD, byte[] oldMD) {
        boolean bResult = true;

        int len = newMD.length;

        if (len != oldMD.length) {
            bResult = false;
        } else {
            for (int i = 0; i < len; i++) {
                if (oldMD[i] != newMD[i]) {
                    bResult = false;

                    break;
                }
            }
        }

        return bResult;
    }

    public static boolean verify(String strNewDigest, String strOldDigest)
            throws Exception {
        return verify(strNewDigest.getBytes("UTF-8"),
                strOldDigest.getBytes("UTF-8"));
    }

    public static String getBase64HashString(String str)
            throws Exception {
        return new String(EncodeUtil.encodeBase64(messageSHA1(str)));
    }

    public static void main(String[] args) throws Exception {
        String SysID = "1100000000000001";
        String TimeStamp = "2010-05-17 00:00:00";
        String ReturnURL = "http://www.g.cn";
        System.out.println(getBase64HashString(SysID + TimeStamp + ReturnURL));
    }
}
