package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.Trade;

public interface TradeMapper {
    int insert(Trade record);

    int insertSelective(Trade record);
}