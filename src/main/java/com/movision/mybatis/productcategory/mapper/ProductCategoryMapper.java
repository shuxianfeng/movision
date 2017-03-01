package com.movision.mybatis.productcategory.mapper;

import com.movision.mybatis.productcategory.entity.ProductCategory;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ProductCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductCategory record);

    int insertSelective(ProductCategory record);

    ProductCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductCategory record);

    int updateByPrimaryKey(ProductCategory record);

    List<ProductCategory> findAllProductCategory(RowBounds rowBounds);//类别查询

    List<ProductCategory> findAllCategoryCondition(Map map, RowBounds rowBounds);//搜索

    int deleteCategory(Integer id);//删除分类

}