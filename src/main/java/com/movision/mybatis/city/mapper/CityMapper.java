package com.movision.mybatis.city.mapper;

import com.movision.mybatis.city.entity.City;

public interface CityMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);
}