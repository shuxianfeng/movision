package com.movision.mybatis.count.mapper;

import com.movision.mybatis.count.entity.Count;

public interface CountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Count record);

    int insertSelective(Count record);

    Count selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Count record);

    int updateByPrimaryKey(Count record);

    int updateTakeCount(int id);

    int updateAccessCount(int id);
}