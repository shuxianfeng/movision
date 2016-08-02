package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;

import java.util.List;
import java.util.Map;

public interface CheckBrandMapper {

    int insertSelective(CheckBrand record);

    CheckBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CheckBrand record);

    int deleteBrand(String id);

    List<Map<String,Object>> searchMyBrand(Map<String,Object> map);

    Brand queryBrandById(String id);
}