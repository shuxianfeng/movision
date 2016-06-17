package com.zhuhuibao.common.util;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 转换工具类 {code-->name}
 *
 * @author jianglz
 * @version 2016-05-19
 */
public class ConvertUtil {


    /**
     * @param map    需要转换的原map对象
     * @param key    需要转换的key
     * @param clzName    类名
     * @param method 方法名
     * @param params 参数列表
     * @return
     */
    public static Map<String, Object> execute(Map<String, Object> map, String key, String clzName, String method, Object[] params) {

        Map<String, String> resultMap = (Map<String, String>) load(clzName, method, params);
        String name = resultMap.get("name");
        map.put(key + "Name", name);
        return map;
    }

    /**
     * 加载指定类的指定方法
     *
     * @param clzName        类名
     * @param methodName 方法名
     * @param params     参数列表
     * @return
     */
    private static Object load(String clzName, String methodName, Object[] params) {
        Object result = null;

        try {
            WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
            Object obj = wac.getBean(clzName);

            Class clz = obj.getClass();

            // 根据方法名获取指定方法的参数类型列表
            Class paramTypes[] = getParamTypes(clz, methodName);
            Method method = clz.getMethod(methodName, paramTypes);
            method.setAccessible(true);

            result = method.invoke(obj, params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 获取参数类型，返回值保存在Class[]中
     *
     * @param cls   类名
     * @param mName 方法名称
     * @return
     */
    public static Class[] getParamTypes(Class cls, String mName) {
        Class[] cs = null;

        /*
        * Note: 由于我们一般通过反射机制调用的方法，是非public方法
        * 所以在此处使用了getDeclaredMethods()方法
        */
        Method[] mtd = cls.getDeclaredMethods();
        for (Method aMtd : mtd) {
            if (!aMtd.getName().equals(mName)) {    // 不是我们需要的参数，则进入下一次循环
                continue;
            }

            cs = aMtd.getParameterTypes();
        }
        return cs;
    }

}
