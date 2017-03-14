package com.movision.mybatis.orders.mapper;

import com.movision.mybatis.orders.entity.Orders;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrdersMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Orders record);

    int insertSelective(Orders record);

    int insertOrders(Orders orders);

    List<Orders> queryOrdersListByIds(int[] ids);

    Orders selectByPrimaryKey(Integer id);

    String queryMaxOrderNumber(String paramstr);

    int updateByPrimaryKeySelective(Orders record);

    int updateByPrimaryKey(Orders record);

    List<Orders> findAllMyOrderList(RowBounds rowBounds, Map map);
}