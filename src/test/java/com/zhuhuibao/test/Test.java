package com.zhuhuibao.test;


import com.zhuhuibao.mybatis.memCenter.entity.MemShopCheck;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jianglz
 * @since 14/11/7.
 */
public class Test /*extends BaseSpringContext*/ {


    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        MemberShop shop = new MemberShop();
        shop.setCompanyAccount("18652093798");
        shop.setCompanyName("WHEO");

        MemShopCheck check = new MemShopCheck();
        PropertyUtils.copyProperties(check,shop);

        System.out.println(shop.getCompanyName());
        System.out.println(check.getCompanyName());
    }

}
