package com.movision.mybatis.take.mapper;

import com.movision.mybatis.take.entity.Take;
import com.movision.mybatis.take.entity.TakeVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface TakeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Take record);

    int insertSelective(Take record);

    Take selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Take record);

    int updateByPrimaryKey(Take record);

    int deleteTakePeople(int id);

    List<TakeVo> findAllTake(RowBounds rowBounds, Take take);

    List<TakeVo> findAllTakeCondition(RowBounds rowBounds, Map map);

    Take queryTakeById(Integer id);
}