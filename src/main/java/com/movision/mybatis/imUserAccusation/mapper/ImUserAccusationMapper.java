package com.movision.mybatis.imUserAccusation.mapper;

import com.movision.mybatis.imUserAccusation.entity.ImUserAccPage;
import com.movision.mybatis.imUserAccusation.entity.ImUserAccusation;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface ImUserAccusationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImUserAccusation record);

    int insertSelective(ImUserAccusation record);

    ImUserAccusation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImUserAccusation record);

    int updateByPrimaryKey(ImUserAccusation record);

    List<ImUserAccusation> queryNotHandleSelectiveRecord(ImUserAccusation imUserAccusation);

    List<ImUserAccPage> findAllImuserAccusation(RowBounds rowBounds, Map map);
}