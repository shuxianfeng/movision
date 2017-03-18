package com.movision.mybatis.orderLogistics.mapper;

import com.movision.mybatis.orderLogistics.entity.OrderLogistics;

public interface OrderLogisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderLogistics record);

    int insertSelective(OrderLogistics record);

    OrderLogistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderLogistics record);

    int updateByPrimaryKey(OrderLogistics record);
}