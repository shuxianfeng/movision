package com.movision.mybatis.constant.mapper;

import com.movision.mybatis.constant.entity.Constant;

import java.util.List;
import java.util.Map;

public interface ConstantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Constant record);

    int insertSelective(Constant record);

    Constant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Constant record);

    int updateByPrimaryKey(Constant record);

    List<Constant> queryRewordList();
}