package com.zhuhuibao.utils;

import java.util.UUID;

/**
 * @author jianglz
 * @since 16/6/23.
 */
public class UUIDGenerator {

    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };
    /**
     * 获取默认UUID
     * @return
     */
    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取UUID去除"-"
     * @return
     */
    public static String genUUIDRemoveSep() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        // 去掉"-"符号
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }

    /**
     * 获得指定数量的UUID
     * @param number
     * @return
     */
    public static String[] genUUID(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = genUUID();
        }
        return ss;
    }
    /**
     * 获得指定数量的UUID 去除"-"
     * @param number
     * @return
     */
    public static String[] genUUIDRemoveSep(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = genUUIDRemoveSep();
        }
        return ss;
    }


    /**
     * 获取8位的UUID
     * @return
     */
    public static String genShortUuid() {
        StringBuilder shortBuffer = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }
    

    public static void main(String[] args) {
//        String[] ss = genUUIDRemoveSep(8);
//        for (int i = 0; i < ss.length; i++) {
//            System.out.println("ss["+i+"]====="+ss[i]);
//        }
        System.out.println(genShortUuid());
    }
}
