package com.movision.mybatis.fuwuhao.mapper;

import com.movision.mybatis.fuwuhao.entity.Fuwuhao;

public interface FuwuhaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Fuwuhao record);

    int insertSelective(Fuwuhao record);

    Fuwuhao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Fuwuhao record);

    int updateByPrimaryKey(Fuwuhao record);

    String openidByUnionid(String openid);
}