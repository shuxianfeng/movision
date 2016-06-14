package com.zhuhuibao.mybatis.oms.mapper;

import java.util.List;
 
import java.util.Map;

import com.zhuhuibao.mybatis.oms.entity.PlatformStatistics;


/**
 * 
 * @author Administrator
 *
 */
public interface PlatformStatMapper {
	 
    /**
     * 查询平台统计数据
     * @return
     */
	List<PlatformStatistics> findAllPlatformStatistics();
	/**
	 * 统计待处理的数据
	 * @return
	 */
	Map<String, String> findAllPlatformWaitStat();
 
}
