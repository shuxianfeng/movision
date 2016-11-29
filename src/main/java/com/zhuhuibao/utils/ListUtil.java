package com.zhuhuibao.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/22
 * Time: 20:51
 */
public class ListUtil {

    /**
     * 判断一个集合是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List list){
        if(null == list || list.size() <= 0 ){
            return true;
        }
        return false;
    }

    /**
     * 判断集合是否不为空
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list){
        if(null != list && list.size() >= 1){
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        List list = new ArrayList();
        System.out.print(isNotEmpty(list));
    }

}
