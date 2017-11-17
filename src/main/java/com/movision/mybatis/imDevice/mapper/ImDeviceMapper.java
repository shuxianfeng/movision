package com.movision.mybatis.imDevice.mapper;

import com.movision.mybatis.imDevice.entity.ImDevice;
import com.movision.mybatis.imuser.entity.ImdeviceAppuser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImDeviceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImDevice record);

    int insertSelective(ImDevice record);

    ImDevice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImDevice record);

    int updateByPrimaryKey(ImDevice record);

    Integer isExistDevice(@Param("deviceid") String deviceid);

    ImDevice selectByDevice(@Param("deviceid") String deviceid);

    int updateImDevice(ImDevice imDevice);

    List<ImdeviceAppuser> selectRelatedAppuserAndImdevice();
}