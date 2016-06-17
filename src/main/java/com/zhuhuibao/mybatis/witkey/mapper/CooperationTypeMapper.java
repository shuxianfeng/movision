package com.zhuhuibao.mybatis.witkey.mapper;

import com.zhuhuibao.mybatis.witkey.entity.CooperationType;

import java.util.List;

public interface CooperationTypeMapper {
    List<CooperationType> findCooperationType();

    List<CooperationType> findSubCooperationType();
}