package com.zhuhuibao.mybatis.witkty.mapper;

import com.zhuhuibao.mybatis.witkty.entity.CooperationType;

import java.util.List;

public interface CooperationTypeMapper {
    List<CooperationType> findCooperationType();

    List<CooperationType> findSubCooperationType();
}