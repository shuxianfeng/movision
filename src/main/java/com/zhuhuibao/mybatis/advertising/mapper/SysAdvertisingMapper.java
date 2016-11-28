package com.zhuhuibao.mybatis.advertising.mapper;

import java.util.List;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import org.apache.ibatis.annotations.Param;

public interface SysAdvertisingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAdvertising record);

    int insertSelective(SysAdvertising record);

    SysAdvertising selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAdvertising record);

    int updateByPrimaryKey(SysAdvertising record);

    List<SysAdvertising> findListByCondition(@Param("chanType") String chanType, @Param("page") String page, @Param("advArea") String advArea);

    List<SysAdvertising> findHottestPosition(@Param("chanType") String chanType, @Param("page") String page, @Param("advArea") String advArea);
}