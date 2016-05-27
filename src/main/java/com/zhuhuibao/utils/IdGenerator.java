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


    public static void main(String[] args) {
        System.out.println(Math.random());

    }

}
