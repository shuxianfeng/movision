package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.CooperationType;
import com.zhuhuibao.mybatis.memCenter.entity.Position;

import java.util.List;

public interface CooperationTypeMapper {
    List<CooperationType> findCooperationType();

    List<CooperationType> findSubCooperationType();
}