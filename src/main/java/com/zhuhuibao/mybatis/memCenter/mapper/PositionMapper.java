package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Position;

import java.util.List;

public interface PositionMapper {
    List<Position> findPosition(int size);

    List<Position> findSubPosition();
}