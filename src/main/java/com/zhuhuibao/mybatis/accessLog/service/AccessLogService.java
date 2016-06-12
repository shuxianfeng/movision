package com.zhuhuibao.mybatis.accessLog.service;

import com.zhuhuibao.mybatis.accessLog.entity.AccessLog;
import com.zhuhuibao.mybatis.accessLog.mapper.AccessLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccessLogService {
	private static final Logger log = LoggerFactory.getLogger(AccessLogService.class);
	
	@Autowired
	private AccessLogMapper accessLogMapper;
	
	/**
	 * 增加访问日志
	 */
	public int addAccessLog(AccessLog accesslog)
	{
		log.debug("增加访问日志");
		int result = 0;
		result = accessLogMapper.addAccessLog(accesslog);
		return result;
	}
	
}