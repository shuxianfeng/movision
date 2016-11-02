package com.zhuhuibao.utils;

import javax.servlet.http.HttpServletRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 校验工具类
 * @author zhuangyuhao
 * @time   2016年11月2日 上午10:12:25
 *
 */
public class ValidateUtils {
	
	/**
	 * 校验手机
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号  
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }  
	
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
