package com.zhuhuibao.mybatis.oms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.oms.entity.PlatformStatistics;
import com.zhuhuibao.mybatis.oms.mapper.PlatformStatMapper;

/**
 * 
 * @author gmli 2016.06.13
 *
 */
 @Service
 @Transactional
public class PlatformStatService {
	 private final static Logger log = LoggerFactory.getLogger(TenderTonedService.class);

    @Autowired
    private PlatformStatMapper platformStatMapper;
    /**
	 * 平台统计信息查询
	 * 
	 * @author gmli
	 */
	public  Map<String,List<PlatformStatistics>> findAllPlatformStat() {
		 List<PlatformStatistics> statList=null;
		 //处理查询统计分类
		 Map<String,List<PlatformStatistics>> resultStatMap=new HashMap();
		 List<PlatformStatistics> statMap=new ArrayList<PlatformStatistics>();
		 
		try{
			statList= platformStatMapper.findAllPlatformStatistics() ;
			if(statList!=null&&statList.size()>0)
			{
				for(int i=0;i<statList.size();i++)
				{
					 
						
						if(resultStatMap.get("resultStat"+statList.get(i).getStat_type())!=null)
						{
							statMap.add(statList.get(i));
						    resultStatMap.put("resultStat"+statList.get(i).getStat_type(), statMap);
						}else{
							
							statMap=new ArrayList<PlatformStatistics>();
							statMap.add(statList.get(i));
							resultStatMap.put("resultStat"+statList.get(i).getStat_type(), statMap);
						}
					 
				}
			}
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
 
		return resultStatMap;
	}
	/**
	 * 待处理数据查询
	 * 
	 * @author gmli
	 */
	public Map<String, String> findAllPlatformWaitStat() {
		Map<String, String> waitList=null;
		try{
			waitList=platformStatMapper.findAllPlatformWaitStat();
		} catch (Exception e) {

			log.error(e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return waitList;
	}
}
