package com.zhuhuibao.mybatis.tech.mapper;

import com.zhuhuibao.mybatis.tech.entity.TechDownLoadData;

public interface TechDownLoadDataMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(TechDownLoadData record);

    TechDownLoadData selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TechDownLoadData record);

    int updateByPrimaryKey(TechDownLoadData record);
}