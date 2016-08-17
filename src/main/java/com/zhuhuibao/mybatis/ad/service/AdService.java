package com.zhuhuibao.mybatis.ad.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.ad.mapper.AdMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
/**
 * 广告service
 * @author Administrator
 *
 */
@Service
@Transactional
public class AdService {
	
	private static final Logger log = LoggerFactory.getLogger(AdService.class);
	@Autowired
	private AdMapper adMapper;
	/**
	 * 查询广告列表
	 * @param pager
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> findAllAdList(Paging pager,
			Map<String, Object> map) {

        try{
        	if(map.get("type")==null)
        	{
        		return adMapper.findAllAdList(pager.getRowBounds(),map);
        	}else{
        		return adMapper.findAllAdDetailsList(pager.getRowBounds(),map);
        	}
            
        }catch (Exception e){
            log.error("AdService::AdService::"+"publisher="+ShiroUtil.getCreateID(),e);
            
            throw e;
        }
	}
	
	/**
	 * 查询广告类型
	 * @param map
	 * @return
	 */
	public List<Map<String, String>> findAdType(Map<String, Object> map) {

        try{
            return adMapper.findAdType(map);
        }catch (Exception e){
            log.error("AdService::findAdType::"+"publisher="+ShiroUtil.getCreateID(),e);
            
            throw e;
        }
	}
	
    //修改广告信息
	
	public int updateAdInfo(Map<String, Object> map) {
		int result = 0;
		try {
			//同步更新广告详情
			String type=map.get("type").toString();
			if(!"1".equals(type))
			{
				adMapper.updateAdDetailsInfo(map);
			}
			
			if("5".equals(type)){
				
				adMapper.updateAdDetails(map);
			}else{
			
			result = adMapper.updateAdInfo(map);
			}
			
		} catch (Exception e) {
			log.error("AdService::updateAdInfo",e);
			throw e;
		}
	  return result;
	}
    /**
     *添加广告信息
     * @param map
     */
	public int addAdInfo(Map<String, Object> map) {
		int result = 0;
		try {
			result = adMapper.addAdInfo(map);
			//同步更新广告详情
			String type=map.get("type").toString();
			if(result>0&&"4".equals(type))
			{
				adMapper.addAdDetailsInfo(map);
			}
		} catch (Exception e) {
			log.error("AdService::addAdInfo",e);
			throw e;
		}
	  return result;
		
	}

	public int deleteAdvDetails(String id) {
		int result = 0;
		try {
			result = adMapper.deleteByPrimaryKey(id);
			//同步更新广告详情 
			 
		} catch (Exception e) {
			log.error("AdService::deleteAdvDetails",e);
			throw e;
		}
	  return result;
	}
	
    /**
     * 添加广告详情
     * @param map
     */
	public int addAdDetails(Map<String, Object> map) {
		int result = 0;
		try {
			result=adMapper.addAdDetails(map); 
		} catch (Exception e) {
			log.error("AdService::addAdDetailsInfo",e);
			throw e;
		}
	    return result;
		
	}
    /**
     * 修改广告详情
     * @param map
     * @return
     */
	public int updateAdDetails(Map<String, Object> map) {
		 
		int result = 0;
		try {
		 
			 
			result=adMapper.updateAdDetails(map);
			 
		} catch (Exception e) {
			log.error("AdService::updateAdInfo",e);
			throw e;
		}
	  return result;
	}
    /**
     * 查询管理ID是否存在
     * @param map
     * @return
     */
	public int queryIdExits(Map<String, Object> map) {
		 
		int result = 0;
		try {
		  result=adMapper.queryIdExits(map);
			 
		} catch (Exception e) {
			log.error("AdService::queryIdExits",e);
			throw e;
		}
	  return result;
	}

	 

}
