package com.movision.mybatis.orderSubLogistics.mapper;

import com.movision.mybatis.orderSubLogistics.entity.OrderSubLogistics;

public interface OrderSubLogisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSubLogistics record);

    int insertSelective(OrderSubLogistics record);

    OrderSubLogistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSubLogistics record);

    int updateByPrimaryKey(OrderSubLogistics record);
}