package com.zhuhuibao.mybatis.ad.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;




/**
 * 广告接口
 */


public interface AdMapper {
  
	List<Map<String, String>> findAllAdList(RowBounds rowBounds,
			Map<String, Object> map);

	List<Map<String, String>> findAdType(Map<String, Object> map);

	int updateAdInfo(Map<String, Object> map);

	void updateAdDetailsInfo(Map<String, Object> map);

	int addAdInfo(Map<String, Object> map);

	void addAdDetailsInfo(Map<String, Object> map);

}
