package com.zhuhuibao.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

public class DesEdeKeyTool {
    private static String m_strKeyArithmetic = "DESEDE";
    private static int m_nKeyLength = 168;

    public static Key generateKey(byte[] seed)
            throws Exception {
        SecureRandom sr = new SecureRandom(seed);
        KeyGenerator kGen = KeyGenerator.getInstance(m_strKeyArithmetic);
        kGen.init(m_nKeyLength, sr);
        Key key = kGen.generateKey();
        return key;
    }

    public static byte[] generateKeyBytes(byte[] seed)
            throws Exception {
        Key key = generateKey(seed);
        return key.getEncoded();
    }

    public static void generateKeyFile(String fileName, byte[] seed)
            throws Exception {
        Key key = generateKey(seed);
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(Hex.encode(key.getEncoded()));
        out.close();
    }

    public static Key byte2Key(byte[] keyBytes)
            throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(keyBytes);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(m_strKeyArithmetic);
        return keyfactory.generateSecret(spec);
    }

    public static Key getKeyFromFile(String fileName)
            throws Exception {
        FileInputStream in = new FileInputStream(fileName);
        byte[] keybytes = new byte[in.available()];
        in.read(keybytes);
        in.close();
        return byte2Key(Hex.decode(new String(keybytes)));
    }
}
