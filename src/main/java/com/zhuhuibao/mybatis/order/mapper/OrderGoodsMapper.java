package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.OrderGoods;

import java.util.List;

public interface OrderGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderGoods record);

    int insertSelective(OrderGoods record);

    OrderGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderGoods record);

    int updateByPrimaryKey(OrderGoods record);

    OrderGoods findByOrderNo(String orderNo);

    List<String> findListByGoodsId(String goodsId);
}