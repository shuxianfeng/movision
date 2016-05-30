package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.OrderGoods;
import com.zhuhuibao.mybatis.order.mapper.OrderGoodsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 订单商品
 */
@Service
public class OrderGoodsService {
    private static final Logger log = LoggerFactory.getLogger(OrderGoodsService.class);

    @Autowired
    OrderGoodsMapper mapper;


    /**
     * 新增订单记录
     *
     * @param record orderGoods
     */
    public void insert(OrderGoods record) {
        int count;
        try {
            count = mapper.insertSelective(record);
            if (count != 1) {
                log.error("t_o_order_goods:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }
}
