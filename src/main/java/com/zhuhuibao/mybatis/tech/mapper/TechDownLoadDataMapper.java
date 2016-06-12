package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TechDownLoadData;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface TechDownLoadDataMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TechDownLoadData record);

    TechDownLoadData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TechDownLoadData record);

    int updateByPrimaryKey(TechDownLoadData record);

    List<Map<String,Object>> findAllDownloadData(RowBounds rowBounds, Map<String,String> map);
}