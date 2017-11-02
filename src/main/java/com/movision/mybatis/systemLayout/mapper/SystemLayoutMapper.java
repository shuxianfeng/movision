package com.movision.mybatis.systemLayout.mapper;

import com.movision.mybatis.systemLayout.entity.SystemLayout;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface SystemLayoutMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SystemLayout record);

    int insertSelective(SystemLayout record);

    SystemLayout selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SystemLayout record);

    int updateByPrimaryKey(SystemLayout record);

    Integer queryRobotSeparate(String separate);

    Double queryRobotpercentage(String percentage);

    String queryServiceUrl(String fileurl);

    Double queryFileRatio(String ratio);

    String queryImgBucket(String img);

    List<SystemLayout> findAllQuerySystemLayotAll(String type, RowBounds rowBounds);

    SystemLayout querySystemLayoutById(Integer id);

    String queryIphonexUrl(String fileurl);

    int queryHaetValue(String value);


}