package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Message;

public interface MessageMapper {
    int saveMessage(Message message);
}