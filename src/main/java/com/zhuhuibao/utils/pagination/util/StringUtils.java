package com.zhuhuibao.utils.pagination.util;

import com.zhuhuibao.mybatis.oms.entity.User;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date Created  2014-2-18
 *
 * @author loafer[zjh527@gmail.com]
 * @version 1.0
 */
public abstract class StringUtils {
    /**
     * <p>Checks if a CharSequence is empty ("") or null.</p>
     * <p/>
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is empty or null
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    /**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     * <p/>
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }

        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 打印实体类信息
     * @param o  javabean对象
     * @return 字符串形式打印对象属性名称和属性值
     */
    public static String beanToString(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = new StringBuffer();
        sb.append("Bean [");
        Field[] farr = o.getClass().getDeclaredFields();
        for (Field field : farr) {
            try {
                field.setAccessible(true);
                sb.append(field.getName());
                sb.append("=");
                if (field.get(o) instanceof Date) {
                    // 日期的处理
                    sb.append(sdf.format(field.get(o)));
                } else {
                    sb.append(field.get(o));
                }
                sb.append("|");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * map对象转字符串
     * @param map 对象
     * @return 返回键值对形式的字符串
     */
    public static String mapToString(Map map) {
        StringBuffer sb = new StringBuffer();
        sb.append("Map:{");
        for (Object obj : map.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key +"="+value);
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }

}
