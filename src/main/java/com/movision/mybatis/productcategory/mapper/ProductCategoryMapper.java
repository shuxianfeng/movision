package com.movision.mybatis.productcategory.mapper;

import com.movision.mybatis.brand.entity.Brand;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
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

    List<GoodsDiscount> findAllGoodsDiscount(RowBounds rowBounds);//活动查询
    List<ProductCategory> findAllCategoryCondition(Map map, RowBounds rowBounds);//搜索

    List<GoodsDiscount> findAllGoodsDiscountCondition(Map map, RowBounds rowBounds);//活动搜索
    int deleteCategory(Integer id);//删除分类

    int addCategory(ProductCategory productCategory);//添加商品分类

    int addBrand(Brand brand);//添加品牌

    int addGoodsDiscount(GoodsDiscount goodsDiscount);//添加商品活动
    ProductCategory queryCategory(Integer id);//根据id查询类别信息

    GoodsDiscount queryGoodsDiscount(Integer id);//根据id查询活动信息
    Brand queryBrand(Integer id);//根据id查询类别信息

    int updateCategory(ProductCategory productCategory);//编辑分类

    int updateDiscount(GoodsDiscount goodsDiscount);//编辑活动
    int updateBrand(Brand brand);//编辑品牌
    List<Brand> findAllBrand(RowBounds rowBounds);//查询品牌列表

    List<Brand> findAllBrandCondition(Map map, RowBounds rowBounds);//品牌搜索

    int updateStop(Integer id);//停用

    int updateUp(Integer id);//启用

    int updateDownD(Integer id);//活动停用

    int updateUpD(Integer id);//活动启用

}