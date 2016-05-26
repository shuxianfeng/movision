package com.zhuhuibao.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static AtomicInteger ID = new AtomicInteger(0);

    private static final String TAG = "zhb";

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
        str = TAG + getYMD() + String.format("%04d", ID.getAndIncrement());

        return str;
    }

    /**
     * Method getYMD.
     *
     * @return String
     */
    private static String getYMD() {
        SimpleDateFormat fymd = new SimpleDateFormat("yyMMddHHmmss");
        Date date = new Date();
        return fymd.format(date);
    }
}
