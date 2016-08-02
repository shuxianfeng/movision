package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.mybatis.memCenter.mapper.CheckBrandMapper;
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

    public Brand queryBrandById(String id) {
        try {
            return checkBrandMapper.queryBrandById(id);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }
}
