package com.movision.mybatis.subOrder.mapper;

import com.movision.mybatis.subOrder.entity.SubOrder;

import java.util.List;

public interface SubOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubOrder record);

    int insertSelective(SubOrder record);

    void batchInsertOrders(List<SubOrder> subOrderList);

    SubOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubOrder record);

    int updateByPrimaryKey(SubOrder record);
}