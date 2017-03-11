package com.movision.mybatis.imSystemInform.mapper;

import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ImSystemInformMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImSystemInform record);

    int insertSelective(ImSystemInform record);

    ImSystemInform selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImSystemInform record);

    int updateByPrimaryKey(ImSystemInform record);

    List<ImSystemInform> selectAll(RowBounds rowBounds);
}