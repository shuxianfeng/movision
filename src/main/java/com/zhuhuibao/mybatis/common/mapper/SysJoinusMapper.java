package com.zhuhuibao.mybatis.common.mapper;

import com.zhuhuibao.mybatis.common.entity.SysJoinus;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface SysJoinusMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysJoinus record);

    int insertSelective(SysJoinus record);

    SysJoinus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysJoinus record);

    int updateByPrimaryKey(SysJoinus record);

    List<SysJoinus> findAllPager(RowBounds rowBounds, Map<String, Object> condition);
}