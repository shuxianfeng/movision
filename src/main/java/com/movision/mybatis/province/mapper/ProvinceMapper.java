package com.movision.mybatis.province.mapper;

import com.movision.mybatis.province.entity.Province;

public interface ProvinceMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(Province record);

    int insertSelective(Province record);

    Province selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Province record);

    int updateByPrimaryKey(Province record);
}