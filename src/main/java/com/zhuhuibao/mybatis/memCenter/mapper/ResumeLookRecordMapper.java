package com.zhuhuibao.mybatis.memCenter.mapper;

import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/6/14 0014.
 */
public interface ResumeLookRecordMapper {

    int addLookRecord(Map<String,Object> map);

    List<Map<String,String>> findAllMyResumeLookRecord(RowBounds rowBounds,Map<String,Object> map);

}
