package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.AlipayRefundCallbackLog;

public interface AlipayRefundCallbackLogMapper {
    int deleteByPrimaryKey(String batchNo);

    int insert(AlipayRefundCallbackLog record);

    int insertSelective(AlipayRefundCallbackLog record);

    AlipayRefundCallbackLog selectByPrimaryKey(String batchNo);

    int updateByPrimaryKeySelective(AlipayRefundCallbackLog record);

    int updateByPrimaryKey(AlipayRefundCallbackLog record);
}