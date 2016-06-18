package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.InvoiceRecord;
/**
 * 发票历史记录信息
 * @author  penglong
 * @create 2016-06-27
 */
public interface InvoiceRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(InvoiceRecord record);

    InvoiceRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(InvoiceRecord record);

    int updateIsRecentUsed(InvoiceRecord record);

}