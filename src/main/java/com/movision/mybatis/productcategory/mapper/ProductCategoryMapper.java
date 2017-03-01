package com.movision.mybatis.productcategory.mapper;

import com.movision.mybatis.brand.entity.Brand;
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

    int addCategory(ProductCategory productCategory);//添加商品分类

    ProductCategory queryCategory(Integer id);//根据id查询类别信息

    int updateCategory(ProductCategory productCategory);//编辑分类

    List<Brand> findAllBrand(RowBounds rowBounds);//查询品牌列表

    List<Brand> findAllBrandCondition(Map map, RowBounds rowBounds);//品牌搜索

}