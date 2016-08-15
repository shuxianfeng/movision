package com.zhuhuibao.mybatis.common.mapper;

import com.zhuhuibao.mybatis.common.entity.SendMsgMobile;

import java.util.List;
import java.util.Map;

public interface SendMsgMobileMapper {
    int insert(SendMsgMobile record);

    int insertSelective(SendMsgMobile record);

    List<Map<String,String>> find();
}