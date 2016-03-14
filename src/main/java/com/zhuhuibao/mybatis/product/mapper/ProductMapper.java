package com.zhuhuibao.mybatis.product.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    List<Product> findAllByPager(RowBounds rowBounds,Product product);
    
    ProductWithBLOBs queryProductByParam(ProductWithBLOBs product);
}