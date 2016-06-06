package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.OrderSms;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderSmsMapper {
    int deleteByPrimaryKey(String orderNo);

    int insert(OrderSms record);

    int insertSelective(OrderSms record);

    OrderSms selectByPrimaryKey(String orderNo);

    int updateByPrimaryKeySelective(OrderSms record);

    int updateByPrimaryKey(OrderSms record);

    List<OrderSms> findByOrderNoAndStatusCode(@Param("orderNos") List<String> orderNos,
                                              @Param("status") String status,
                                              @Param("templateCode") String templateCode);
}