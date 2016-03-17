package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Brand;

import java.util.List;

public interface BrandMapper {

    int addBrand(Brand brand);

    int updateBrand(Brand brand);

    int deleteBrand(Brand brand);

    List<Brand> searchBrandByStatus(Brand brand);

    int searchBrandSize(Brand brand);

    Brand brandDetails(int id);
    
}