package com.movision.mybatis.dinyuehao.mapper;

import com.movision.mybatis.dinyuehao.entity.Dinyuehao;

public interface DinyuehaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dinyuehao record);

    int insertSelective(Dinyuehao record);

    Dinyuehao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dinyuehao record);

    int updateByPrimaryKey(Dinyuehao record);


    int unionidByOpenid(String unionid);
}