package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.Refund;

public interface RefundMapper {
    int deleteByPrimaryKey(String batchNo);

    int insert(Refund record);

    int insertSelective(Refund record);

    Refund selectByPrimaryKey(String batchNo);

    int updateByPrimaryKeySelective(Refund record);

    int updateByPrimaryKey(Refund record);
}