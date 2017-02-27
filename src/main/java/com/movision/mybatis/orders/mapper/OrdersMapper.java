package com.movision.mybatis.orders.mapper;

import com.movision.mybatis.orders.entity.Orders;
import org.apache.ibatis.session.RowBounds;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;

public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    Orders selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> findAllMyOrderList(RowBounds rowBounds, Map map);
}