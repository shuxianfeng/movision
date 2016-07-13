package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.BrandBean;
import com.zhuhuibao.common.pojo.BrandDetailBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.pojo.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.product.entity.Product;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BrandMapper {

    int addBrand(Brand brand);

    int updateBrand(Brand brand);

    int deleteBrand(String id);

    List<Brand> searchBrandByStatus(Brand brand);

    List<Brand> searchBrand(Brand brand);

    List<Brand> findAllByPager(RowBounds rowBounds,Brand brand);

    int searchBrandSize(Brand brand);

    int findBrandSize(Brand brand);

    Brand brandDetails(String id);

    List<ResultBean> findAllBrand(Product product);

    List<ResultBean> findBrandByScateid(Product product);

    List<ResultBean> searchSuggestBrand();

    List<BrandBean> searchAll();

    List<SuggestBrand> SuggestBrand();

    BrandDetailBean details(String id);

    List<Map<String,Object>> queryBrandProductAgentCount(Long createId);

    List<Map<String,String>> queryRecommendBrand(Map<String,Object> map);
}