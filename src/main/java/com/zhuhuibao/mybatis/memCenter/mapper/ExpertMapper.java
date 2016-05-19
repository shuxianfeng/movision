package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Expert;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ExpertMapper {
    int expertMapper(Expert expert);

    Expert queryExpertById(String id);

    int updateExpert(Expert expert);

    List<Expert> findAllExpertList(RowBounds rowBounds,Map<String,Object> map);

    List<Expert> queryHotExpert(int count);

    List<Expert> queryLatestExpert(int count);
}