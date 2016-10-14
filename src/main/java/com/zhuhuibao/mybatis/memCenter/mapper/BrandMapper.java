package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.BrandBean;
import com.zhuhuibao.common.pojo.BrandDetailBean;
import com.zhuhuibao.common.pojo.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface BrandMapper {

    int addBrand(Brand brand);

    int updateBrand(Brand brand);

    int deleteBrand(String id);

    List<Brand> searchBrandByStatus(Brand brand);

    List<Brand> findAllByPager(RowBounds rowBounds, Brand brand);

    int searchBrandSize(Brand brand);

    Brand brandDetails(String id);

    List<Map<String, Object>> findAllBrand(String scateid);

    List<Map<String, Object>> findBrandByScateid(String scateid);

    List<Map<String, Object>> searchSuggestBrand();

    List<BrandBean> searchAll();

    List<SuggestBrand> SuggestBrand();

    BrandDetailBean details(String id);

    List<Map<String, Object>> queryBrandProductAgentCount(Long createId);

    List<Map<String, String>> queryRecommendBrand(Map<String, Object> map);

    List<Map<String, String>> findByKeyword(@Param("keyword") String keyword, @Param("count") Integer count);

    /**
     * 根据一级分类分页获取品牌信息
     * 
     * @param map
     * @return
     */
    List<SuggestBrand> selHotBrandListByType(RowBounds rowBounds,Map<String, Object> map);
}