package com.movision.utils;

/**
 * @Author shuxf
 * @Date 2017/3/6 19:02
 * 工具类：根据两个地点的经纬度计算两个点的里程
 * 单位为：米
 */
public class CalculateDistance {

    private static double rad(double d) {
        return d * 3.141592653589793D / 180.0D;
    }

    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2.0D * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0D), 2.0D) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.pow(Math.sin(b / 2.0D), 2.0D)));
        s *= 6378137.0D;
        s = Math.round(s * 10000.0D) / 10000L;
        return s;
    }
}
