package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckSysBrand;
import com.zhuhuibao.mybatis.memCenter.mapper.CheckBrandMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.CheckSysBrandMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 品牌信息
 */
@Service
@Transactional
public class CheckBrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    private CheckBrandMapper checkBrandMapper;

    @Autowired
    private CheckSysBrandMapper checkSysBrandMapper;

    public int addBrand(CheckBrand brand) {
        try {
            checkBrandMapper.insertSelective(brand);
            return brand.getId();
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int updateBrand(CheckBrand brand) {
        try {
            return checkBrandMapper.updateByPrimaryKeySelective(brand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int deleteBrand(String id) {
        try {
            return checkBrandMapper.deleteBrand(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<Map<String,Object>> searchMyBrand(Map<String,Object> map) {
        try {
            return checkBrandMapper.searchMyBrand(map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public CheckBrand queryBrandById(String id) {
        try {
            return checkBrandMapper.queryBrandById(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<CheckBrand> searchBrandByPager(Paging<CheckBrand> pager, Map<String,Object> map) {
        try {
            return checkBrandMapper.findAllByPager(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int deleteBrandSysByBrandID(Integer id) {
        try {
            return checkSysBrandMapper.deleteBrandSysByBrandID(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public int addSysBrand(CheckSysBrand sysBrand) {
        try {
            return checkSysBrandMapper.insertSelective(sysBrand);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }
}
