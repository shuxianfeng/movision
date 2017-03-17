package com.movision.mybatis.logisticsCompany.mapper;

import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;

public interface LogisticsCompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsCompany record);

    int insertSelective(LogisticsCompany record);

    LogisticsCompany selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogisticsCompany record);

    int updateByPrimaryKey(LogisticsCompany record);
}