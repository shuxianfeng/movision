package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.SysBrand;

public interface SysBrandMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteBrandSysByBrandID(Integer id);

    int insert(SysBrand record);

    int insertSelective(SysBrand record);

    SysBrand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysBrand record);

    int updateByPrimaryKey(SysBrand record);
}