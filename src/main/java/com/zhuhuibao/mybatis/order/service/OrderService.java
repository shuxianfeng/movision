package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.mybatis.order.mapper.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单处理
 */
@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Resource
    OrderMapper orderMapper;



}
