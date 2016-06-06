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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<DictionaryTechData> getFirstCategory()
    {
        log.info("select tech data first category");
        List<DictionaryTechData> firstCategoryList;
        try {
            firstCategoryList = dicTDMapper.selectFirstCategory();
        }catch(Exception e){
            log.error("select tech data first category error!",e);
            throw e;
        }
        return firstCategoryList;
    }

    /**
     * 查询二级分类
     * @param firstCategoryId 一级分类ID
     * @return
     */
    public List<DictionaryTechData> getSecondCategory(int firstCategoryId)
    {
        log.info("select tech data second category firstCategoryId = "+firstCategoryId);
        List<DictionaryTechData> secondCategoryList;
        try {
            secondCategoryList = dicTDMapper.selectSecondCategoryById(firstCategoryId);
        }catch(Exception e){
            log.error("select tech data second category error!",e);
            throw e;
        }
        return secondCategoryList;
    }

    /**
     * 查询上传资料的分类信息
     * @param firstCategoryId 一级分类ID
     * @return
     */
    public List<Map<String,Object>> selectCategoryInfo(int firstCategoryId)
    {
        log.info("select category info firstCategoryId = "+firstCategoryId);
        List<Map<String,Object>> fcateList = new ArrayList<Map<String,Object>>();
        try {
            List<Map<String,Object>> categoryList = dicTDMapper.selectCategoryInfo(firstCategoryId);
            List<Map<String,Object>> scateList = null;
            Map<String,Object> fcateMap = null;
            Map<String,Object> scateMap = null;
            if(!categoryList.isEmpty())
            {
                int size = categoryList.size();
                for(int i=0;i<size;i++)
                {
                    Map<String,Object> category = categoryList.get(i);
                    Integer parentId = (Integer) category.get("parentId");
                    if(fcateMap != null && fcateMap.get("fCode")!= null && fcateMap.get("fCode") == parentId)
                    {
                        scateList = (List<Map<String, Object>>) fcateMap.get("list");
                        scateMap = new HashMap<String,Object>();
                        scateMap.put("sName",category.get("name"));
                        scateMap.put("sCode",category.get("code"));
                        scateList.add(scateMap);
                        fcateMap.put("list",scateList);
                    }
                    else
                    {

                        fcateMap =  new HashMap<String,Object>();
                        fcateMap.put("fName",category.get("firstName"));
                        fcateMap.put("fCode",category.get("parentId"));
                        scateMap = new HashMap<String,Object>();
                        scateList = new ArrayList<Map<String,Object>>();
                        scateMap.put("sName",category.get("name"));
                        scateMap.put("sCode",category.get("code"));
                        scateList.add(scateMap);
                        fcateMap.put("list",scateList);
                        fcateList.add(fcateMap);
                    }
                }
            }
        }catch(Exception e){
            log.error("select category info error!",e);
            throw e;
        }
        return fcateList;
    }
}
