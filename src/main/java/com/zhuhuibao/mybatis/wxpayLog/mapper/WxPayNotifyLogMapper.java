package com.zhuhuibao.mybatis.wxpayLog.mapper;

import com.zhuhuibao.mybatis.wxpayLog.entity.WxPayNotifyLog;

public interface WxPayNotifyLogMapper {
    int deleteByPrimaryKey(String transactionId);

    int insert(WxPayNotifyLog record);

    int insertSelective(WxPayNotifyLog record);

    WxPayNotifyLog selectByPrimaryKey(String transactionId);

    int updateByPrimaryKeySelective(WxPayNotifyLog record);

    int updateByPrimaryKey(WxPayNotifyLog record);
}