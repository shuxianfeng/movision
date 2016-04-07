package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.BrandBean;
import com.zhuhuibao.common.BrandDetailBean;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌业务处理
 * Created by cxx on 2016/3/23 0023.
 */
@Service
@Transactional
public class BrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据会员id，状态status(可以为空)查询品牌
     */
    public List<Brand> searchBrandByStatus(Brand brand)
    {
        List<Brand> brands = brandMapper.searchBrandByStatus(brand);
        return brands;
    }

    public List<Brand> searchBrand(Brand brand)
    {
        List<Brand> brands = brandMapper.searchBrand(brand);
        return brands;
    }

    /**
     * 查询拥有产品的品牌
     */
    public List<BrandBean> searchAll()
    {
        log.debug("查询拥有产品的品牌");
        List<BrandBean> brandList = brandMapper.searchAll();
        return brandList;
    }

    /**
     * 查询拥有产品的推荐品牌
     */
    public List<SuggestBrand> SuggestBrand()
    {
        log.debug("查询拥有产品的推荐品牌");
        List<SuggestBrand> brandList = brandMapper.SuggestBrand();
        return brandList;
    }

    /**
     * 查询二级系统下所有品牌,返回id,品牌logo
     */
    public List<ResultBean> findAllBrand(Product product)
    {
        log.debug("查询二级系统下所有品牌");
        List<ResultBean> brandList = brandMapper.findAllBrand(product);
        return brandList;
    }

    /**
     * 查询二级系统下所有品牌，返回id，name
     */
    public List<ResultBean> findBrandByScateid(Product product)
    {
        log.debug("查询二级系统下所有品牌");
        List<ResultBean> brandList = brandMapper.findBrandByScateid(product);
        return brandList;
    }

    /**
     * 查询推荐品牌
     */
    public List<ResultBean> searchSuggestBrand()
    {
        log.debug("查询推荐品牌");
        List<ResultBean> brandList = brandMapper.searchSuggestBrand();
        return brandList;
    }

    /**
     * 查询品牌详情
     */
    public BrandDetailBean details(String id)
    {
        log.debug("查询品牌详情");
        BrandDetailBean brand = brandMapper.details(id);
        return brand;
    }

    /**
     * 新建品牌
     */
    public int addBrand(Brand brand)
    {
        log.debug("新建品牌");
        int isAdd = brandMapper.addBrand(brand);
        return isAdd;
    }

    /**
     * 更新品牌
     */
    public int updateBrand(Brand brand)
    {
        log.debug("更新品牌");
        int isUpdate = brandMapper.updateBrand(brand);
        return isUpdate;
    }

    /**
     * 删除品牌
     */
    public int deleteBrand(String id)
    {
        log.debug("删除品牌");
        int isDelete = brandMapper.deleteBrand(id);
        return isDelete;
    }

    /**
     * 品牌数量
     */
    public int searchBrandSize(Brand brand)
    {
        log.debug("品牌数量");
        int size = brandMapper.searchBrandSize(brand);
        return size;
    }

    /**
     * 查询品牌详情
     */
    public Brand brandDetails(int id)
    {
        log.debug("查询品牌详情");
        Brand brand = brandMapper.brandDetails(id);
        return brand;
    }
}
