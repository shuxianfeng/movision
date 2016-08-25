package com.zhuhuibao.mybatis.activity.mapper;

import com.zhuhuibao.mybatis.activity.entity.ActivityApply;

public interface ActivityApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityApply record);

    int insertSelective(ActivityApply record);

    ActivityApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityApply record);

    int updateByPrimaryKey(ActivityApply record);

    int getApplyCount();
}