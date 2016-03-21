package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.BrandBean;
import com.zhuhuibao.common.BrandDetailBean;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.product.entity.Product;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface BrandMapper {

    int addBrand(Brand brand);

    int updateBrand(Brand brand);

    int deleteBrand(Brand brand);

    int batchDeleteBrand(String id);

    List<Brand> searchBrandByStatus(Brand brand);

    int searchBrandSize(Brand brand);

    Brand brandDetails(int id);

    List<ResultBean> findAllBrand(Product product);

    List<ResultBean> searchSuggestBrand();

    List<BrandBean> searchAll();

    List<SuggestBrand> SuggestBrand();

    BrandDetailBean details(String id);
}