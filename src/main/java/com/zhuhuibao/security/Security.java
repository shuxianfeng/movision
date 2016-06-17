package com.zhuhuibao.security;

import java.security.Key;

public class Security {
    public static final byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public static String generalStringFor3DES(String keyValue, String For3DES, String ForDigest, byte[] keyIV, String linkString) throws Exception {
        String rtn = null;
        String tempcheck = null;
        String _For3DES = null;
        try {
            Key key = get3DESKey(keyValue);

            if ((ForDigest != null) && (ForDigest.length() > 0)) {
                tempcheck = DigestForString.message(ForDigest, "BASE64");
                _For3DES = For3DES + linkString + tempcheck;
            } else {
                _For3DES = For3DES;
            }
            byte[] encryptStr = Base64.encode(EncryptionForString.encrypt(keyIV, _For3DES, key, "RAW"));

            rtn = new String(encryptStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return rtn;
    }

    public static String Decrypt3DES2String(String keyValue, String DES2String, byte[] keyIV) throws Exception {
        String DecryptString = new String(decrypt(DES2String.getBytes(),
                keyValue, keyIV), "UTF-8");
        return DecryptString;
    }

    private static Key get3DESKey(String keyValue) throws Exception {
        return DesEdeKeyTool.byte2Key(Hex.decode(keyValue));
    }

    private static byte[] decrypt(byte[] encryptByBase64Str, String keyValue, byte[] keyIV) throws Exception {
        byte[] encryptStr = Base64.decode(encryptByBase64Str);
        Key key = get3DESKey(keyValue);

        byte[] decryptStr = EncryptionForString.decrypt(keyIV, encryptStr, key);
        return decryptStr;
    }

    public static String getEncryptString(String key, String in) {
        try {
            return generalStringFor3DES(key, in == null ? "" : in, null, iv, "$");
        } catch (Exception localException) {
        }
        return null;
    }

    public static String getDecryptString(String key, String in) {
        try {
            return Decrypt3DES2String(key, in, iv);
        } catch (Exception localException) {
        }
        return null;
    }
}
