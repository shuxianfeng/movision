package com.zhuhuibao.mybatis.product.mapper;

import java.util.*;

import org.apache.ibatis.session.RowBounds;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.oms.entity.CategoryAssemble;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductMap;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);

    List<Product> findAllByPager(RowBounds rowBounds,Product product);
    
    //根据参数信息查询所有产品
    List<Product> queryProductByParamIDs(String paramIDs);
    
    List<ResultBean> getSCateListByBrandId(Product product);
    
    List<ProductWithBLOBs> queryProductInfoBySCategory(Map<String,Object> map);
    
    //查询推荐热点品牌
    List<ProductMap> queryRecommendHotProduct(Map<String,Object> map);
    
    List<CategoryAssemble> findSecondCategoryBrand();
    
    List<CategoryAssemble> findCategoryAssemble();
    
    int updateHit(Long id);
    
    int batchUnpublish(List<String> list);
    
    ProductWithBLOBs queryPrdDescParamService(Long id);
}