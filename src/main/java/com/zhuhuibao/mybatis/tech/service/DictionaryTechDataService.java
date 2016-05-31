package com.zhuhuibao.mybatis.tech.service;/**
 * @author Administrator
 * @version 2016/5/31 0031
 */

import com.zhuhuibao.mybatis.tech.entity.DictionaryTechData;
import com.zhuhuibao.mybatis.tech.mapper.DictionaryTechDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *技术资料分类业务处理类
 *@author pl
 *@create 2016/5/31 0031
 **/
@Service
@Transactional
public class DictionaryTechDataService {
    private final static Logger log = LoggerFactory.getLogger(DictionaryTechDataService.class);

    @Autowired
    DictionaryTechDataMapper dicTDMapper;

    /**
     * 查询一级分类
     * @return
     */
    public DictionaryTechData getFirstCategory()
    {
        log.info("select tech data first category");
        DictionaryTechData firstCategory;
        try {
            firstCategory = dicTDMapper.selectFirstCategory();
        }catch(Exception e){
            log.error("select tech data first category error!",e);
            throw e;
        }
        return firstCategory;
    }

    /**
     * 查询二级分类
     * @param firstCategoryId 一级分类ID
     * @return
     */
    public DictionaryTechData getSecondCategory(int firstCategoryId)
    {
        log.info("select tech data second category firstCategoryId = "+firstCategoryId);
        DictionaryTechData secondCategory;
        try {
            secondCategory = dicTDMapper.selectSecondCategoryById(firstCategoryId);
        }catch(Exception e){
            log.error("select tech data second category error!",e);
            throw e;
        }
        return secondCategory;
    }
}
