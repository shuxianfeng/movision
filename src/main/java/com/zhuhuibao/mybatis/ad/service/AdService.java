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
        	if(map.get("type")!=null)
        	{
        		return adMapper.findAllAdList(pager.getRowBounds(),map);
        	}else{
        		return adMapper.findAllAdList(pager.getRowBounds(),map);
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
			result = adMapper.updateAdInfo(map);
			//同步更新广告详情
			String type=map.get("type").toString();
			if(result>0&&!"1".equals(type))
			{
				adMapper.updateAdDetailsInfo(map);
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
			if(result>0&&"3".equals(type))
			{
				adMapper.addAdDetailsInfo(map);
			}
		} catch (Exception e) {
			log.error("AdService::addAdInfo",e);
			throw e;
		}
	  return result;
		
	}

}
