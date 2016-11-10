package com.zhuhuibao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.tech.service.OrderManagerService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@Service
@Transactional
public class MobileOrderService {

    @Autowired
    private OrderManagerService orderManagerService;

    /**
     * 查询制定会员的订单信息
     * 
     * @param memberId
     * @param status
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<Map<String, String>> getOrderList(Long memberId, String status, String pageNo, String pageSize) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("status", status);
        condition.put("buyerId", memberId);

        Paging<Map<String, String>> orderPager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, String>> orderList = orderManagerService.findAllOmsTechOrder(orderPager, condition);
        orderPager.result(orderList);

        return orderPager;
    }

    /**
     * 查询订单详情
     * 
     * @param orderNo
     * @param memberId
     * @return
     */
    public Map<String, Object> getOrderDetail(String orderNo, Long memberId) {
        return orderManagerService.getOrderDetail(orderNo, memberId);
    }

    /**
     * 查询收银台初始信息 1:培训课程购买使用筑慧币消费的情况. 0:VIP充值，筑慧币购买不使用筑慧币的情况
     * 
     * @param orderNo
     * @return
     */
    public Map<String, Object> getCashierDeskInfo(String orderNo) {
        return orderManagerService.selectCashierDeskInfo(orderNo);
    }
}
