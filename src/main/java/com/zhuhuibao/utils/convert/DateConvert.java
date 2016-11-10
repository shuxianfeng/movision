package com.zhuhuibao.utils.convert;

import org.apache.commons.beanutils.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 把yyyy-MM-dd HH:mm:ss形式的日期字符串转换成Date类型
 * @author jianglz
 * @since 16/5/27.
 */
public class DateConvert implements Converter {

    private static final Logger logger = LoggerFactory.getLogger(DateConvert.class);

    private final static String pattern = "yyyy-MM-dd HH:mm:ss";
    @Override
    public Object convert(Class type, Object value) {
        if (value == null) {
            return (null);
        }

        Date dateObj = null;
        if (value instanceof String) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                dateObj = sdf.parse((String) value);
            } catch (ParseException pe) {
                logger.error("日期类型转换异常");
                return (null);
            }
        }

        return dateObj;
    }
}
