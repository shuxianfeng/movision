package com.zhuhuibao.mybatis.product.mapper;

import com.zhuhuibao.mybatis.product.entity.ProductParam;

import java.util.List;

public interface ProductParamMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductParam record);

    int insertSelective(ProductParam record);

    ProductParam selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductParam record);

    int updateByPrimaryKey(ProductParam record);
    
    List<ProductParam> selectParamByIds(List<Integer> list);
}