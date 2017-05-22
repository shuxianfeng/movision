package com.movision.mybatis.imuser.mapper;

import com.movision.mybatis.deviceAccid.entity.DeviceAccid;
import com.movision.mybatis.imuser.entity.ImUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImUserMapper {
    int insert(ImUser record);

    int insertSelective(ImUser record);

    ImUser selectByUserid(@Param("userid") Integer id, @Param("type") Integer type);

    List<ImUser> selectAllAPPImuser();

    Integer queryUserByAccid(String accid);

}