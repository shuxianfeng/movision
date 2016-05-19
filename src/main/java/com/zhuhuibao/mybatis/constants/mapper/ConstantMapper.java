package com.zhuhuibao.mybatis.constants.mapper;

import com.zhuhuibao.mybatis.constants.entity.Constant;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ConstantMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Constant record);

    int insertSelective(Constant record);

    Constant selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Constant record);

    int updateByPrimaryKey(Constant record);

    List<Constant> selectByType(String type);

    Map<String,String> selectByTypeCode(@Param("type") String type , @Param("code") String code);
}