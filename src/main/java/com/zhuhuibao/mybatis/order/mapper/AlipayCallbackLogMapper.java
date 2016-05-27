package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.AlipayCallbackLog;

public interface AlipayCallbackLogMapper {
    int deleteByPrimaryKey(String tradeNo);

    int insert(AlipayCallbackLog record);

    int insertSelective(AlipayCallbackLog record);

    AlipayCallbackLog selectByPrimaryKey(String tradeNo);

    int updateByPrimaryKeySelective(AlipayCallbackLog record);

    int updateByPrimaryKey(AlipayCallbackLog record);
}