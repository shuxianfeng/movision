package com.movision.facade.pay;

import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.service.OrderService;
import com.movision.mybatis.pay.WepayContent;
import com.movision.utils.propertiesLoader.AlipayPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 11:41
 */
@Service
public class WepayFacade {

    private static Logger log = LoggerFactory.getLogger(WepayFacade.class);

    @Autowired
    private OrderService orderService;

    /**
     * 用户拼装微信支付的请求入参（仅适用于小程序的租赁业务）
     * @param ordersid
     * @return
     */
    public Map<String, Object> packageWePayParam(String ordersid) {

        Map<String, Object> map = new HashMap<>();//用于返回结果的map

        String[] ordersidstr = ordersid.split(",");
        int[] ids = new int[ordersidstr.length];
        for (int i = 0; i < ordersidstr.length; i++) {
            ids[i] = Integer.parseInt(ordersidstr[i]);
        }

        //根据订单id查询所有主订单列表
        List<Orders> ordersList = orderService.queryOrdersListByIds(ids);

        if (null != ordersList && ordersList.size() == ordersidstr.length) {//传入的订单均存在且均为待支付的情况下

            String alipaygateway = AlipayPropertiesLoader.getValue("alipay_gateway");//支付宝请求网关

            List<WepayContent> wepayContentList = new ArrayList<>();//wepay返回content实体列表

            for (int j = 0; j < ids.length; j++){

                double totalamount = 0;//当前订单的实际支付总金额

                if (ordersList.get(j).getId() == ids[j]) {
                    //计算实际支付总金额
                    totalamount = ordersList.get(j).getMoney();
                    if (ordersList.get(j).getIsdiscount() == 1) {
                        totalamount = totalamount - ordersList.get(j).getDiscouponmoney();
                    }
                    if (null != ordersList.get(j).getDispointmoney()) {
                        totalamount = totalamount - ordersList.get(j).getDispointmoney();
                    }
                }

                log.info("打印订单号为：" + ids[j] + "的实际需要支付的总金额=================================>" + totalamount);

            }
        }
        return map;
    }
}
