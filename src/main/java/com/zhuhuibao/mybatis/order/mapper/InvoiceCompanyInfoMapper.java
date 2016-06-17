package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.InvoiceCompanyInfo;

public interface InvoiceCompanyInfoMapper {
    int insert(InvoiceCompanyInfo record);

    int insertSelective(InvoiceCompanyInfo record);
}