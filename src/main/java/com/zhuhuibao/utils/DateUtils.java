package com.zhuhuibao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String date2Str(Date target, String pattern)
    {
        if (null == target)
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String targetStr = "";
        try
        {
            targetStr = sdf.format(target);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return targetStr;
    }
    
    public static Date str2Date(String target, String pattern)
    {
        if (null == target)
        {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try
        {
            date = sdf.parse(target);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return date;
    }
    
    public static String str2DateFormat(String src)
    {
        if (src == null || "".equals(src))
        {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            long seconds = 0;
            seconds = Long.parseLong(src);
            return sdf.format(new Date(seconds));
        }
        catch (NumberFormatException e)
        {
            return src;
        }
    }
    
    /**
     * 时间加减计算
     * @param date
     * @param offset
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Date date2Sub(Date date, int field, int offset)
    {
        Calendar c = Calendar.getInstance();
        
        c.setTime(date);
        c.add(field, offset);
        
        return c.getTime();
    }

    public static void main(String[] args) {
    	Date date = DateUtils.str2Date("2016-02-01 14:21:34","yyyy-MM-dd HH:mm:ss");
    	Date date1 = new Date();
    	System.out.println(date1.before(date));
	}
}
