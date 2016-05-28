package com.zhuhuibao.test;

import com.zhuhuibao.mybatis.order.entity.Order;
import com.zhuhuibao.utils.convert.BeanUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jianglz
 * @since 16/5/27.
 */
public class MapTest {

    public static void main(String[] args) throws Exception {

        Order order = new Order();

        Map<String,Object> map = new HashMap<>();
        map.put("orderNo","2016098765728803");
        map.put("buyerId","1");
        map.put("amount","100.00");
        map.put("dealTime","20160527");
        map.put("payMode","1");
        map.put("status","2");
        map.put("goodsType","1");
        map.put("desciptions","测试");

        order = (Order) BeanUtil.mapToObject(map, Order.class);

        System.out.println("--- Map2Bean Map Info: ");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("--- Bean Info: ");
        System.out.println("orderNo: " + order.getOrderNo());

       Map mp =  BeanUtil.objectToMap(order);

    }

}
