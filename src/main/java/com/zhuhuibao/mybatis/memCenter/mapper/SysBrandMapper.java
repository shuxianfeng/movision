package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.SysBrand;

import java.util.List;
import java.util.Map;

public interface SysBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBrandSysByBrandID(Integer id);

    int insert(SysBrand record);

    int insertSelective(SysBrand record);

    SysBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysBrand record);

    int updateByPrimaryKey(SysBrand record);

    List<Map<String,Object>> queryBrandSysById(String id);
    
    List<String> queryScateByBrandId(Integer brandid);
}