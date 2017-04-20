package com.movision.mybatis.imSystemInform.mapper;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ImSystemInformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImSystemInform record);

    int insertSelective(ImSystemInform record);

    ImSystemInform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImSystemInform record);

    int updateByPrimaryKey(ImSystemInform record);

    List<ImSystemInform> findAll(RowBounds rowBounds);

    List<ImSystemInform> findAllSystemInform(Map map, RowBounds rowBounds);//条件搜索

    ImSystemInform queryBodyAll(Integer id);//查询全部内容

    ImSystemInform queryByUserid();//查询最新一条记录
}