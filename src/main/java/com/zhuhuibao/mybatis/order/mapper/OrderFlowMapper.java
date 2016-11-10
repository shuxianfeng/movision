package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderFlowMapper {
    int insert(OrderFlow record);

    int insertSelective(OrderFlow record);

    OrderFlow findByOrderNoAndTradeMode(@Param("orderNo") String orderNo, @Param("tradeMode") String tradeMode);

    List<OrderFlow> findByOrderNo(@Param("orderNo") String orderNo);

    OrderFlow findUniqueOrderFlow(@Param("orderNo") String orderNo);

    int update(OrderFlow alFlow);
}