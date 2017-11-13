package com.movision.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
                log.info("计算活动剩余结束天数");
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

    /**
     * 获取当前系统的时间戳 （精确到毫秒）ex：2017-06-27 10:57:08 039
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return df.format(new Date());
    }

    /**
     * 获取当前月份的第一天 如：2017-08-01
     *
     * @return
     */
    public static String getCurrentMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = DATE_FORMAT.format(c.getTime());
        log.debug("===============first:" + first);
        return first;
    }

    /**
     * 获取当前月份最后一天 如：2017-08-31
     *
     * @return
     */
    public static String getCurrentMonthLastDay() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = DATE_FORMAT.format(ca.getTime());
        log.debug("===============last:" + last);
        return last;
    }

    /**
     * 获取指定的日期
     * ============date:1990-08-19
     *
     * @return
     */
    public static Date getDefaultBirthday() {
        Calendar ca = new GregorianCalendar();
        ca.set(Calendar.YEAR, 1990);
        ca.set(Calendar.MONTH, 7);
        ca.set(Calendar.DATE, 19);
        log.debug("============date:" + DATE_FORMAT.format(ca.getTime()));
        return ca.getTime();
    }

    /**
     * 比较日期大小
     *
     * @param date yyyy-MM-dd
     * @return 传入的日期大于最大日期 返回1；
     *          传入的日期小于最大日期 返回-1；
     *          传入的日期等于最大日期 返回0；
     * @throws ParseException
     */
    public static Integer compareDateWithCurrentDate(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date maxDate = calendar.getTime();  //最大日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date paramDate = sdf.parse(date + " 00:00:00");

        if (paramDate.after(maxDate)) {
            log.debug("传入的日期大于最大日期");
            return 1;
        } else if (paramDate.before(maxDate)) {
            log.debug("传入的日期小于最大日期");
            return -1;
        } else {
            log.debug("传入的日期等于最大日期");
            return 0;
        }
    }

    /**
     * 比较两个日期
     *
     * @param firstDate  第一个日期 yyyy-MM-dd
     * @param secondDate 第二个日期
     * @return 第一个日期大于第二个日期 返回1；
     * 第一个日期小于第二个日期 返回-1；
     * 第一个日期等于第二个日期 返回0；
     * @throws ParseException
     */
    public static Integer compareDate(String firstDate, String secondDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstD = sdf.parse(firstDate);
        Date secondD = sdf.parse(secondDate);

        if (firstD.after(secondD)) {
            log.debug("第一个日期大于第二个日期");
            return 1;
        } else if (firstD.before(secondD)) {
            log.debug("第一个日期小于第二个日期");
            return -1;
        } else {
            log.debug("第一个日期等于第二个日期");
            return 0;
        }
    }

    /**
     * 比较两个日期
     *
     * @param firstDate
     * @param secondDate
     * @return
     * @throws ParseException
     */
    public static Integer compareDate(String firstDate, Date secondDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date firstD = sdf.parse(firstDate);


        if (firstD.after(secondDate)) {
            log.debug("第一个日期大于第二个日期");
            return 1;
        } else if (firstD.before(secondDate)) {
            log.debug("第一个日期小于第二个日期");
            return -1;
        } else {
            log.debug("第一个日期等于第二个日期");
            return 0;
        }
    }

    public static String[] convertDateArrToStringArr(Date[] dateArr) {
        int len = dateArr.length;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String[] strArr = new String[len];
        for (int i = 0; i < len; i++) {
            strArr[i] = sdf.format(dateArr[i]);
        }
        log.debug("转换后的strArr:" + strArr.toString());
        return strArr;
    }

    /**
     * 将时间转换为时间戳
     *
     * @param s
     * @return String 类型
     * @throws ParseException
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     *
     * @param s
     * @return String 类型
     */
    public static String stampToDateStr(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     *
     * @param s String 类型
     * @return Date 类型
     */
    public static Date stampToDate(String s) {
        return new Date(new Long(s));
    }


    public static void main(String[] args) throws ParseException {

        System.out.println(stampToDate("1510537860000"));
//        System.out.println(dateToStamp("2017-11-13 9:51:00"));

//        compareDate("2017-10-29", "2017-10-28");
//        compareDateWithCurrentDate("2017-10-27");

//        getCurrentMonthFirstDay();
//        getCurrentMonthLastDay();
//        getDefaultBirthday();

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 0);  //Calendar.HOUR_OF_DAY的值 0-23
//        log.debug("当前的时间Calendar.HOUR_OF_DAY：" + calendar.get(Calendar.HOUR_OF_DAY));

    }
}
