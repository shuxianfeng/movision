package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);

    List<ResultBean> findCity(String provincecode);

    City getCityInfo(String code);

    Map<String,String> findCityByCode(@Param("cityCode") String cityCode);
}