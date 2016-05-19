package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PositionMapper {
    List<Position> findPosition(int size);

    List<Position> findSubPosition();

    Map<String,Object> findById(@Param("id") String id);
}