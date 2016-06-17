package com.zhuhuibao.mybatis.order.mapper;

import com.zhuhuibao.mybatis.order.entity.ZhbAccount;

public interface ZhbAccountMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ZhbAccount record);

    int insertSelective(ZhbAccount record);

    ZhbAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ZhbAccount record);

    int updateByPrimaryKey(ZhbAccount record);
}