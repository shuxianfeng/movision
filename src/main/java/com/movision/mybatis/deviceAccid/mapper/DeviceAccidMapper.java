package com.movision.mybatis.deviceAccid.mapper;

import com.movision.mybatis.deviceAccid.entity.DeviceAccid;
import org.apache.ibatis.annotations.Param;

public interface DeviceAccidMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DeviceAccid record);

    int insertSelective(DeviceAccid record);

    DeviceAccid selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DeviceAccid record);

    int updateByPrimaryKey(DeviceAccid record);

    DeviceAccid selectByDeviceno(@Param("deviceno") String deviceno);
}