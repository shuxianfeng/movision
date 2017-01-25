package com.movision.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/1/25 16:38
 */
public class ListUtil {

    /**
     * 判断一个集合是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        return null == list || list.size() <= 0;
    }

    /**
     * 判断集合是否不为空
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list){
        return null != list && list.size() >= 1;
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        System.out.print(isNotEmpty(list));
    }

}
