package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AreaMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);

    List<ResultBean> findArea(String cityCode);

    Area getAreaInfo(String code);

    Map<String,String> findAreaByCode(@Param("areaCode") String areaCode);
}