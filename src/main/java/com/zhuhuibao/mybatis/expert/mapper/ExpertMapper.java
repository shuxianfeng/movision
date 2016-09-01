package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.Expert;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ExpertMapper {
    int expertMapper(Expert expert);

    Expert queryExpertById(String id);

    Expert queryExpertByCreateId(String id);

    Expert queryExpertByCreateid(String createid);

    int updateExpert(Expert expert);

    int updateExpertByCreateid(Expert expert);

    int updateExpertViews(Expert expert);

    List<Expert> findAllExpertList(RowBounds rowBounds, Map<String, Object> map);

    List<Expert> queryHotExpert(int count);

    List<Expert> queryLatestExpert(int count);

    List<Map<String,String>> queryHomepageExpertList(Map<String, Object> map);

    List<Map<String,String>> findAllMyLookedExpertList(RowBounds rowBounds, Map<String, Object> map);

    int deleteLookedExpert(String id);

    List<Map<String,Object>> findAllExpert(RowBounds rowBounds, Map<String, Object> map);
}