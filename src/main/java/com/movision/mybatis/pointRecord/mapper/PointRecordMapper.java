package com.movision.mybatis.pointRecord.mapper;

import com.movision.mybatis.pointRecord.entity.PointRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PointRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PointRecord record);

    int queryIsComment(int userid);

    int insertSelective(PointRecord record);

    PointRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PointRecord record);

    int updateByPrimaryKey(PointRecord record);

    List<PointRecord> queryMyAllTypePoint(@Param("userid") Integer id);

    List<PointRecord> queryMyTodayPoint(@Param("userid") Integer id);

    int queryMyTodayPointSum(Integer userid);

    int queryIsSignToday(@Param("userid") Integer id);

    List<Map> findAllMyPointRecord(RowBounds rowBounds, Map map);

    void inserRecord(Map<String, Object> parammap);

    PointRecord selectFinishPersonDataPointRecord(@Param("userid") Integer id);

    Integer queryCircleOrIndex(Map map);
}