package com.movision.mybatis.orders.service;

import com.movision.mybatis.orders.entity.Orders;
import com.movision.mybatis.orders.mapper.OrdersMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 13:50
 */
@Service
@Transactional
public class OrderService {

    private static Logger log = LoggerFactory.getLogger(OrderService.class);
    @Autowired
    private OrdersMapper ordersMapper;

    public List<Orders> findAllMyOrderList(Paging<Orders> paging, Map<String, Object> map) {
        try {
            log.info("查询我的订单列表");
            return ordersMapper.findAllMyOrderList(paging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询我的订单列表失败", e);
            throw e;
        }
    }

    public Orders getOrderById(int id) {
        try {
            log.info("根据订单id获取订单");
            return ordersMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("根据订单id获取订单失败", e);
            throw e;
        }
    }

}
