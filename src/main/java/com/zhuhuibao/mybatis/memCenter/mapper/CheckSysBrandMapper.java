package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CheckSysBrand;

import java.util.List;
import java.util.Map;

public interface CheckSysBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBrandSysByBrandID(Integer id);

    int insert(CheckSysBrand record);

    int insertSelective(CheckSysBrand record);

    CheckSysBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckSysBrand record);

    int updateByPrimaryKey(CheckSysBrand record);

    List<Map<String,Object>> queryBrandSysById(String id);
}