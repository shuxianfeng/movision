package com.zhuhuibao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static AtomicInteger ID = new AtomicInteger(0);

    private final static String TAG = "zhb";


    /**
     * Method createBatchNo.
     *
     * @return String
     */
    public synchronized static String createBatchNo() {
        String str;

        if (ID.intValue() > 9999) {
            ID.set(0);
        }
        str = getYMD() + String.format("%08d", ID.getAndIncrement());

        return str;
    }
    /**
     * Method createOrderNo.
     *
     * @return String
     */
    public synchronized static String createOrderNo() {
        String str;

        if (ID.intValue() > 9999) {
            ID.set(0);
        }
        str = TAG +  getYMDHMS() + String.format("%04d", ID.getAndIncrement());

        return str;
    }

    /**
     * Method getYMDHMS.
     *
     * @return String
     */
    private static String getYMDHMS() {
        SimpleDateFormat fymd = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        return fymd.format(date);
    }

    private static String getYMD() {
        SimpleDateFormat fymd = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return fymd.format(date);
    }


    /**
     * 生成SN码  {8位字母数字组合}
     * @return
     */
    public static String createSNcode(){
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = 65;//random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }



    public static void main(String[] args) {
        System.out.println(createSNcode());

    }

}
