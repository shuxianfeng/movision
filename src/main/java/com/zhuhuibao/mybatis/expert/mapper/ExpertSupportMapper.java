package com.zhuhuibao.mybatis.expert.mapper;

import com.zhuhuibao.mybatis.expert.entity.ExpertSupport;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ExpertSupportMapper {

    int applyExpertSupport(ExpertSupport expertSupport);

    List<Map<String,String>> findAllExpertSupportListOms(RowBounds rowBounds, Map<String, Object> map);

    int updateExpertSupport(ExpertSupport expertSupport);

    Map<String,String> queryExpertSupportInfoById(String id);

}