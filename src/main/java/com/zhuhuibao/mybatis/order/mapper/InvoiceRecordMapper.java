package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.InvoiceRecord;

import java.util.Map;

/**
 * 发票历史记录信息
 * @author  penglong
 * @create 2016-06-27
 */
public interface InvoiceRecordMapper {
    int deleteByPrimaryKey(Map<String,Object> con);

    int insertSelective(InvoiceRecord record);

    InvoiceRecord selectByPrimaryKey(Map<String,Object> con);

    int updateByPrimaryKeySelective(InvoiceRecord record);

    int updateIsRecentUsed(InvoiceRecord record);

}