package com.movision.mybatis.orderoperation.mapper;

import com.movision.mybatis.orderoperation.entity.Orderoperation;

public interface OrderoperationMapper {
    int insert(Orderoperation record);

    int insertSelective(Orderoperation record);
}