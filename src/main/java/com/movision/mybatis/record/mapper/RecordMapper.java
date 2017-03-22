package com.movision.mybatis.record.mapper;

import com.movision.mybatis.record.entity.Record;
import com.movision.mybatis.record.entity.RecordVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface RecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    List<RecordVo> queryIntegralList(String userid, RowBounds rowBounds);

    int addIntegralRecord(Map map);

    int insertRewardRecord(Map map);
}