package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.pojo.BrandBean;
import com.zhuhuibao.common.pojo.BrandDetailBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.pojo.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.pagination.model.Paging;
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
    public List<Brand> searchBrandByStatus(Brand brand)throws Exception
    {
        try {
            return brandMapper.searchBrandByStatus(brand);
        }catch (Exception e){
            throw e;
        }
    }

    public List<Brand> searchBrand(Brand brand)throws Exception
    {
        try {
            return brandMapper.searchBrand(brand);
        }catch (Exception e){
            throw e;
        }
    }

    public List<Brand> searchBrandByPager(Paging<Brand> pager, Brand brand)throws Exception
    {
        try {
            return brandMapper.findAllByPager(pager.getRowBounds(),brand);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询拥有产品的品牌
     */
    public List<BrandBean> searchAll()throws Exception
    {
        try {
            return brandMapper.searchAll();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询拥有产品的推荐品牌
     */
    public List<SuggestBrand> SuggestBrand()throws Exception
    {
        try {
            return brandMapper.SuggestBrand();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询二级系统下所有品牌,返回id,品牌logo
     */
    public List<ResultBean> findAllBrand(Product product)throws Exception
    {
        try {
            return brandMapper.findAllBrand(product);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询二级系统下所有品牌，返回id，name
     */
    public List<ResultBean> findBrandByScateid(Product product)throws Exception
    {
        try {
            return brandMapper.findBrandByScateid(product);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询推荐品牌
     */
    public List<ResultBean> searchSuggestBrand()throws Exception
    {
        try {
            return brandMapper.searchSuggestBrand();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询品牌详情
     */
    public BrandDetailBean details(String id)throws Exception
    {
        try {
            return brandMapper.details(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 新建品牌
     */
    public int addBrand(Brand brand)throws Exception
    {
        try {
            return brandMapper.addBrand(brand);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 更新品牌
     */
    public int updateBrand(Brand brand)throws Exception
    {
        try {
            return brandMapper.updateBrand(brand);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 删除品牌
     */
    public int deleteBrand(String id)throws Exception
    {
        try {
            return brandMapper.deleteBrand(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 品牌数量
     */
    public int searchBrandSize(Brand brand)throws Exception
    {
        try {
            return brandMapper.searchBrandSize(brand);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 品牌数量
     */
    public int findBrandSize(Brand brand)throws Exception
    {
        try {
            return brandMapper.findBrandSize(brand);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询品牌详情
     */
    public Brand brandDetails(int id)throws Exception
    {
        try {
            return brandMapper.brandDetails(id);
        }catch (Exception e){
            throw e;
        }
    }
}
