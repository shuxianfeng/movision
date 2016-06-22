package com.zhuhuibao.mybatis.product.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.oms.entity.CategoryAssemble;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.mybatis.product.entity.ProductMap;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.entity.ProductWithMember;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductWithBLOBs record);

    int insertSelective(ProductWithBLOBs record);

    ProductWithBLOBs selectByPrimaryKey(Long id);

    ProductWithMember selectProductMemberByid(Long id);

    int updateByPrimaryKeySelective(ProductWithBLOBs record);
    
    int updateProductStatus(ProductWithBLOBs record);

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
    
    List<ResultBean> findSubSystem(String id);

    List<Product> findProductBySystemId(String id);

    List<Map<String,String>> queryProductTypeListByCompanyId(Map<String,Object> queryMap);

    List<Map<String,String>> queryHotProductListByCompanyId(Map<String,Object> queryMap);

    List<Map<String,String>> queryLatestProductListByCompanyId(Map<String,Object> queryMap);
}