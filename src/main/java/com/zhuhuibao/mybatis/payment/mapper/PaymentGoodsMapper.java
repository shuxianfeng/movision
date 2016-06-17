package com.zhuhuibao.mybatis.payment.mapper;

import com.zhuhuibao.mybatis.payment.entity.PaymentGoods;

import java.util.Map;

/**
 * 平台筑慧币消费记录
 * @author  penglong
 * @create 2016-06-217
 */
public interface PaymentGoodsMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(PaymentGoods record);

    PaymentGoods selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaymentGoods record);

    int checkIsViewGoods(Map<String,Object> map);
}