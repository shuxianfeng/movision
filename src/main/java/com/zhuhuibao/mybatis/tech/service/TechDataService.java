package com.zhuhuibao.mybatis.tech.service;/**
 * @author Administrator
 * @version 2016/5/31 0031
 */

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.mybatis.tech.entity.TechCategoryBean;
import com.zhuhuibao.mybatis.tech.entity.TechData;
import com.zhuhuibao.mybatis.tech.mapper.TechDataMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *技术资料业务处理类
 *@author pl
 *@create 2016/5/31 0031
 **/
@Service
@Transactional
@RequestMapping(value="")
public class TechDataService {

    private final static Logger log = LoggerFactory.getLogger(TechDataService.class);

    @Autowired
    TechDataMapper techDataMapper;

    @Autowired
    SiteMailService siteMailService;

    /**
     * 运营管理平台技术资料搜索
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findAllOMSTechCooperationPager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all OMS tech data for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.findAllOMSTechDataPager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all OMS tech data for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 技术频道技术资料搜索
     * @param condition 搜索条件
     * @return
     */
    public List<TechData> findAllTechDataPager(Paging<TechData> pager, Map<String,Object> condition)
    {
        log.info("find all tech data for pager "+ StringUtils.mapToString(condition));
        List<TechData> techList = null;
        try{
            techList = techDataMapper.findAllTechDataPager(pager.getRowBounds(),condition);
            List<Map<String,String>> scategoryList = techDataMapper.findScategory(condition);
            if(!scategoryList.isEmpty())
            {
                List<TechCategoryBean> categoryList = new ArrayList<TechCategoryBean>();
                for(Map<String,String> cate : scategoryList)
                {
                    TechCategoryBean bean = new TechCategoryBean();
                    bean.setCode(String.valueOf(cate.get("sCategory")));
                    bean.setName(cate.get("name"));
                    categoryList.add(bean);
                }
                if(!techList.isEmpty())
                {
                    for(TechData tech : techList)
                    {
                        tech.setCategoryList(categoryList);
                    }
                }
            }
        }catch(Exception e)
        {
            log.error("find all tech data for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 技术频道搜索行业类别
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findScategory(Map<String,Object> condition)
    {
        log.info("find all scategory for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.findScategory(condition);
        }catch(Exception e)
        {
            log.error("find all scategory for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 插入技术资料：行业解决方案，技术文档，培训资料
     * @param techData
     * @return
     */
    public int insertTechData(TechData techData)
    {
        int result = 0;
        log.info("insert tech data info "+ StringUtils.beanToString(techData));
        try {
            result = techDataMapper.insertSelective(techData);
        }catch(Exception e)
        {
            log.error("insert data cooperation info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 注销技术资料
     * @param condition
     * @return
     */
    public int deleteTechData(Map<String, Object> condition)
    {
        int result;
        log.info("delete oms tech data "+StringUtils.mapToString(condition));
        try{
            result = techDataMapper.deleteByPrimaryKey(condition);
        }catch (Exception e){
            log.error("delete oms tech data error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新技术资料
     * @param techData
     * @return
     */
    public int updateTechData(TechData techData)
    {
        int result;
        log.info("update oms tech data "+StringUtils.beanToString(techData));
        try{
            result = techDataMapper.updateByPrimaryKeySelective(techData);
            if("3".equals(String.valueOf(techData.getStatus())))
            {
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(), techData.getCreateid(),techData.getReason());
            }
        }catch (Exception e){
            log.error("update oms tech data error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新点击率或者下载率
     * @param map
     * @return
     */
    public int updateTechDataViewsOrDL(Map<String,Object> map)
    {
        int result;
        log.info("update tech data views or download rate "+StringUtils.mapToString(map));
        try{
            result = techDataMapper.updateTechDataViewsOrDL(map);
        }catch (Exception e){
            log.error("update tech data views or download rate error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 查询技术资料信息
     * @param id 技术资料ID
     * @return
     */
    public TechData selectTechDataInfo(Long id)
    {
        TechData techData;
        log.info("select oms tech data "+id);
        try{
            techData = techDataMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            log.error("select oms tech data error! ",e);
            throw e;
        }
        return techData;
    }

    /**
     * 会员中心编辑技术资料信息
     * @param id 技术资料ID
     * @return
     */
    public Map<String,String> selectMCTechDataDetail(Long id)
    {
        Map<String,String> techData;
        log.info("select mc tech data "+id);
        try{
            techData = techDataMapper.selectMCTechDataDetail(id);
        }catch (Exception e){
            log.error("select mc tech data error! ",e);
            throw e;
        }
        return techData;
    }

    /**
     * 获取技术资料文件名称
     * @param id 技术资料ID
     * @return
     */
    public String selectTechDataAttachName(Long id)
    {
        String fileName = null;
        log.info("select tech data attach file name "+id);
        try{
            TechData techData = techDataMapper.selectByPrimaryKey(id);
            if(techData != null)
            {
                fileName = techData.getAttach();
            }
        }catch (Exception e){
            log.error("select tech data attach file name error! ",e);
            throw e;
        }
        return fileName;
    }

    /**
     * 预览技术资料详情
     * @param id 技术资料ID
     * @return
     */
    public List<Map<String,String>> previewTechDataDetail(Long id)
    {
        log.info("preview tech data detail id = "+id);
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.previewTechDataDetail(id);
        }catch(Exception e)
        {
            log.error("preview tech data detail error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 查询解决方案，技术资料，培训资料的点击排行
     * @param map
     * @return
     */
    public List<Map<String,String>> findDataViewsOrder(Map<String,Object> map)
    {
        log.info("find views order "+StringUtils.mapToString(map));
        List<Map<String,String>> dataList = null;
        try{
            dataList = techDataMapper.findViewsOrder(map);
        }catch(Exception e)
        {
            log.error("find views order error!",e);
            throw e;
        }
        return dataList;
    }

    /**
     * 查询解决方案，技术资料，培训资料的下载排行
     * @param map
     * @return
     */
    public List<Map<String,String>> findDownloadOrder(Map<String,Object> map)
    {
        log.info("find views order "+StringUtils.mapToString(map));
        List<Map<String,String>> dataList = null;
        try{
            dataList = techDataMapper.findDownloadOrder(map);
        }catch(Exception e)
        {
            log.error("find views order error!",e);
            throw e;
        }
        return dataList;
    }

    /**
     * 查询技术频道首页的解决方案，技术资料，培训资料
     * @param map
     * @return
     */
    public Map<String,List<Map<String,String>>> findIndexTechData(Map<String,Object> map)
    {
        log.info("find home page tech data "+StringUtils.mapToString(map));
        Map<String,List<Map<String,String>>> techMap;
        try{
            List<Map<String,String>> dataList = techDataMapper.findIndexTechData(map);
            techMap = generateDataInfo(dataList,"sCategory");
        }catch(Exception e)
        {
            log.error("find home page tech data error!",e);
            throw e;
        }
        return techMap;
    }

    /**
     * 生产技术频道首页展示的数据结构
     * @param dataList
     * @param type
     * @return
     */
    public Map<String,List<Map<String,String>>> generateDataInfo(List<Map<String, String>> dataList,String type) {
        Map<String, List<Map<String, String>>> map = new HashMap<>();
        if(!dataList.isEmpty()) {
            List<Map<String, String>> infoList;
            for (Map<String, String> tData : dataList) {
                String sCategory = String.valueOf(tData.get(type));
                if (map.get(sCategory) != null) {
                    infoList = map.get(sCategory);
                    infoList.add(tData);
                } else {
                    infoList = new ArrayList<>();
                    infoList.add(tData);
                    map.put(sCategory, infoList);
                }
            }
        }
        return map;
    }

    /**
     * 查询技术频道个人站点信息
     * @param createId
     * @return
     */
    public Map<String,String> findTechSiteInfo(Long createId)
    {
        log.info("find tech site info"+createId);
        Map<String,String> techMap;
        try{
            techMap = techDataMapper.findTechSiteInfo(createId);
        }catch(Exception e)
        {
            log.error("find tech site info error1!",e);
            throw e;
        }
        return techMap;
    }

    /**
     * 查询技术频道资料总数
     * @return
     */
    public Map<String,String> findTechCount()
    {
        log.info("find tech site total");
        Map<String,String> techMap;
        try{
            techMap = techDataMapper.findTechCount();
        }catch(Exception e)
        {
            log.error("find tech site total error1!",e);
            throw e;
        }
        return techMap;
    }

    /**
     * 会员中心首页查询技术资料信息
     * @param createId
     * @return
     */
    public Map<String,Object> findDataUploadDownloadCount(Long createId)
    {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try {
            List<Map<String, Object>> mapList = techDataMapper.queryDataUploadDownloadCount(createId);
            Map<String,Object> map1 = mapList.get(0);
            resultMap.put("uploadCount",map1.get("count"));
            Map<String,Object> map2 = mapList.get(1);
            resultMap.put("downloadCount",map2.get("count"));
        }catch(Exception e){
            log.error("find data upload download error!",e);
            throw e;
        }
        return resultMap;
    }
}
