package com.zhuhuibao.test;


import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.service.course.CourseService;
import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jianglz
 * @since 16/6/14.
 */
public class OrderTest extends BaseSpringContext {

    public static void main(String[] args) {

    }

    @Autowired
    CourseService courseService;

    @org.junit.Test
    public void testCreateOrder() {
        Map<String,String> msgParam = new HashMap<>();

        msgParam.put("goodsId","1");
        msgParam.put("goodsType","1");
        msgParam.put("number","2");

        msgParam.put("buyerId","1");

        msgParam.put("needInvoice","false");

        msgParam.put("mobile","18652093798");

        msgParam.put("partner", AlipayPropertiesLoader.getPropertyValue("partner"));

        courseService.createOrder(msgParam);
    }
}
