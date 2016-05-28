package com.zhuhuibao.utils.convert;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * bean map 工具类
 */
public class BeanUtil {
    private static final Logger log = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 使用org.apache.commons.beanutils进行转换
     * map转换bean
     *
     * @param map       map
     * @param beanClass bean
     * @return  bean
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        ConvertUtils.register(new DateConvert(), java.util.Date.class);

        Object obj = beanClass.newInstance();

        org.apache.commons.beanutils.BeanUtils.populate(obj, map);

        return obj;
    }

    /**
     * bean转换成map
     *
     * @param obj bean
     * @return map
     */
    public static Map objectToMap(Object obj) {
        if (obj == null)
            return null;

        return new org.apache.commons.beanutils.BeanMap(obj);
    }


}
