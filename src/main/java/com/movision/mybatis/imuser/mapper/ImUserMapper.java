package com.movision.mybatis.imuser.mapper;

import com.movision.mybatis.imuser.entity.ImUser;
import org.apache.ibatis.annotations.Param;

public interface ImUserMapper {
    int insert(ImUser record);

    int insertSelective(ImUser record);

    ImUser selectByUserid(@Param("userid") Integer id);
}