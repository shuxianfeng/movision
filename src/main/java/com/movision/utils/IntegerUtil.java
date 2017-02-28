package com.movision.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/28 16:54
 */
public class IntegerUtil {

    public static boolean isEmpty(Integer integer) {
        return StringUtils.isEmpty(String.valueOf(integer));
    }

    public static boolean isNotEmpty(Integer integer) {
        return StringUtils.isNotEmpty(String.valueOf(integer));
    }
}
