package com.zhuhuibao.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class ValidateUtils {

    /**
     * 校验金额
     *
     * @param money
     * @return
     */
    public static boolean validateMoney(String money) {
        Pattern p = Pattern
                .compile("^([1-9]{1}[0-9]{0,7}(\\.[0-9]{1,2})?|0(\\.[0-9]{1,2})?|[1-9]{1}\\d{0,7})$");
        return p.matcher(money).matches();
    }

    public static void main(String[] args) {
        System.out.println(ValidateUtils.validateMoney("0.0"));
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
