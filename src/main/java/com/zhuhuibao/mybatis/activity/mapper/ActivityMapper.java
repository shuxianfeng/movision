package com.zhuhuibao.mybatis.activity.mapper;

import com.zhuhuibao.mybatis.activity.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);

    Map<String,String> findByOrderNo(String orderNo);

    void updateApplyNum(@Param("id") Long id, @Param("number") int number);
}