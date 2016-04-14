package com.zhuhuibao.mybatis.accessLog.mapper;

import com.zhuhuibao.mybatis.accessLog.entity.AccessLog;

public interface AccessLogMapper {
    //增加访问日志
    int addAccessLog(AccessLog accessLog);
}