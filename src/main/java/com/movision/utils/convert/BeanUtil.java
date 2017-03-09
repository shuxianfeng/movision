package com.movision.utils.convert;

import com.movision.mybatis.imFirstDialogue.entity.ImMsg;
import com.movision.mybatis.user.entity.User;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.HashMap;
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


    /**
     * 将javabean转为map类型，然后返回一个map类型的值
     * 网络上的
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!StringUtils.equals(name, "class")) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * IM使用，IM的javabean转换为map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> ImBeanToMap(Object obj) {

        Map<String, Object> map = objectToMap(obj);
        Map<String, Object> toMap = new HashedMap();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (StringUtils.isNotEmpty(String.valueOf(entry.getValue())) && !String.valueOf(entry.getValue()).equals("null")
                    && !StringUtils.equals(entry.getKey(), "class")) {
                System.out.println(entry.getKey() + ", " + entry.getValue());
                toMap.put(entry.getKey(), entry.getValue());
            }
        }
        return toMap;
    }


    public static void main(String[] args) {
        ImMsg imMsg = new ImMsg();
        imMsg.setFrom("1111");
        imMsg.setOpe(0);
        imMsg.setTo("222");
        imMsg.setType(0);
        imMsg.setBody("lalalalal");
//
        System.out.println(ImBeanToMap(imMsg));

    }

}
