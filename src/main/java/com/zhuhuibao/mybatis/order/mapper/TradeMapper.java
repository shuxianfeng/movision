package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.Trade;

public interface TradeMapper {
    int deleteByPrimaryKey(String tradeNo);

    int insert(Trade record);

    int insertSelective(Trade record);

    Trade selectByPrimaryKey(String tradeNo);

    int updateByPrimaryKeySelective(Trade record);

    int updateByPrimaryKey(Trade record);
}