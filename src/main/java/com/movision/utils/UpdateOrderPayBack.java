package com.movision.utils;

import com.movision.mybatis.orders.service.OrderService;
import com.movision.mybatis.subOrder.entity.SubOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/14 20:55
 */
@Service
public class UpdateOrderPayBack {

    private static Logger log = LoggerFactory.getLogger(UpdateOrderPayBack.class);

    @Autowired
    private OrderService orderService;

    @Transactional
    public void updateOrder(int[] ids, String trade_no, Date intime, int type) {
        //type  支付方式：1支付宝 2微信;   ids 订单号int数组;   trade_no 支付流水号; intime 交易时间
        log.info("更新商品订单>>>>>>>>>>>>>>>>>>>");
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("ids", ids);
        parammap.put("trade_no", trade_no);
        parammap.put("intime", intime);
        parammap.put("type", type);
        orderService.updateOrder(parammap);

        //增加商品销量
        log.info("增加商品销量>>>>>>>>>>>>>>>>>>>");
        //根据订单id查询所有订单中的所有子订单列表（商品列表）
        List<SubOrder> subOrderList = orderService.queryAllSubOrderList(ids);
        for (int i = 0; i < subOrderList.size(); i++) {
            int goodsid = subOrderList.get(i).getGoodsid();//商品id
            int sum = subOrderList.get(i).getSum();//商品件数
            Map<String, Object> map = new HashMap<>();
            map.put("goodsid", goodsid);
            map.put("sum", sum);
            orderService.updateStock(map);
        }

        System.out.println("");
    }

}
