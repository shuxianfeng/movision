package com.movision.mybatis.logisticsCompany.mapper;

import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsCompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogisticsCompany record);

    int insertSelective(LogisticsCompany record);

    LogisticsCompany selectByPrimaryKey(Integer id);

    List<LogisticsCompany> queryLogisticType();

    int updateByPrimaryKeySelective(LogisticsCompany record);

    int updateByPrimaryKey(LogisticsCompany record);
}