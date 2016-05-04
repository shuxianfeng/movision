package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;

public interface CooperationMapper {

    int publishCooperation(Cooperation record);

    Cooperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Cooperation record);

}