package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.Refund;

public interface RefundMapper {
    int insert(Refund record);

    int insertSelective(Refund record);
}