package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CheckBrandMapper {

    int insertSelective(CheckBrand record);

    int updateByPrimaryKeySelective(CheckBrand record);

    int deleteBrand(String id);

    List<Map<String,Object>> searchMyBrand(Map<String,Object> map);

    CheckBrand queryBrandById(String id);

    List<CheckBrand> findAllByPager(RowBounds rowBounds, Brand brand);
}