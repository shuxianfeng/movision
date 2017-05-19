package com.movision.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtils {

    private static Logger log = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 详细时间格式
     */
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");


    public static String date2Str(Date target) {

        return date2Str(target, DATE_FORMAT);
    }

    public static String dateTime2Str(Date target) {

        return date2Str(target, DEFAULT_DATE_FORMAT);
    }

    public static String date2Str(Date target, SimpleDateFormat pattern) {
        if (null == target || null == pattern) {
            return "";
        }
        String targetStr = "";
        try {
            targetStr = pattern.format(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetStr;
    }

    public static String date2Str(Date target, String pattern) {
        if (null == target) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String targetStr = "";
        try {
            targetStr = sdf.format(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetStr;
    }

    public static Date str2Date(String target) {
        return str2Date(target, "yyyy-MM-dd");
    }

    public static Date str2Date(String target, String pattern) {
        if (null == target) {
            return new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String str2DateFormat(String src) {
        if (src == null || "".equals(src)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long seconds = 0;
            seconds = Long.parseLong(src);
            return sdf.format(new Date(seconds));
        } catch (NumberFormatException e) {
            return src;
        }
    }

    public static String str2DateFormat(String src, String pattern) {
        if (src == null || "".equals(src)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            long seconds;
            seconds = Long.parseLong(src);
            return sdf.format(new Date(seconds));
        } catch (NumberFormatException e) {
            return src;
        }
    }

    /**
     * 时间加减计算
     *
     * @param date
     * @param offset
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Date date2Sub(Date date, int field, int offset) {
        Calendar c = Calendar.getInstance();

        c.setTime(date);
        c.add(field, offset);

        return c.getTime();
    }

    /**
     * 相差天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long dayDiff(Date startDate, Date endDate) {
        return (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public static String dateToString(Date date, String resolution) {
        int formatLen;
        switch (resolution) {
            case "YEAR":
                formatLen = 4;
                break;
            case "MONTH":
                formatLen = 6;
                break;
            case "DAY":
                formatLen = 8;
                break;
            case "HOUR":
                formatLen = 10;
                break;
            case "MINUTE":
                formatLen = 12;
                break;
            case "SECOND":
                formatLen = 14;
                break;
            case "MILLISECOND":
                formatLen = 17;
                break;
            default:
                formatLen = 0;
        }
        SimpleDateFormat format;
        format = new SimpleDateFormat("yyyyMMddHHmmssSSS".substring(0, formatLen), Locale.ROOT);
        return format.format(date);
    }

    /**
     * 计算活动距离结束剩余的天数
     *
     * @param now   当前系统时间
     * @param begin 活动开始时间
     * @param end   活动结束时间
     * @return
     * @throws ParseException
     */
    public static int activeEndDays(Date now, Date begin, Date end) {
        int enddays = 0;
        if (now.before(begin)) {
            log.info("活动还未开始");
            enddays = -1;//活动还未开始返回-1
        } else if (end.before(now)) {
            log.info("活动已结束");
            enddays = 0;//活动已结束返回0
        } else if (begin.before(now) && now.before(end)) {
            try {
                log.error("计算活动剩余结束天数");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date a = sdf.parse(sdf.format(now));
                Date b = sdf.parse(sdf.format(end));
                Calendar cal = Calendar.getInstance();
                cal.setTime(a);
                long time1 = cal.getTimeInMillis();
                cal.setTime(b);
                long time2 = cal.getTimeInMillis();
                long between_days = (time2 - time1) / (1000 * 3600 * 24);
                enddays = Integer.parseInt(String.valueOf(between_days));
            } catch (Exception e) {
                log.error("计算活动剩余结束天数失败");
                e.printStackTrace();
            }
        }
        return enddays;
    }

    /**
     * 获取当前距离结束日期的剩余天数
     *
     * @param now
     * @param end
     * @return
     */
    public static Long getBetweenDays(Date now, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date a = null;
        Date b = null;
        try {
            a = sdf.parse(sdf.format(now));
            b = sdf.parse(sdf.format(end));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        long time1 = cal.getTimeInMillis();
        cal.setTime(b);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return between_days;
    }


    public static void main(String[] args) throws ParseException {
//        Date date = DateUtils.date2Sub(DateUtils.str2Date("2016-03-02 20:16:21", "yyyy-MM-dd HH:mm:ss"), 12, 10);
//        System.out.println(date);
        // Date date1 = new Date();
        // System.out.println(date1);
        // System.out.println(date1.before(date));
        // String start = "2016-06-20 16:42:41";
        // String end = "2017-06-21 00:00:00";
        // SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        // Date startTime = sf.parse(start);
        // Date endTime = sf.parse(end);
        // long days = dayDiff(startTime, endTime);
        // System.out.println(days);
//         String s = DateUtils.str2DateFormat("1468466182000", "yyyy-MM-dd");
//         System.out.println(s);

//        System.out.println(DateUtils.str2Date("20170129023128", "yyyyMMddHHmmss"));
        System.out.println(str2Date("1991-07-11"));

    }
}
