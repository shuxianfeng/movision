package com.zhuhuibao.mybatis.dictionary.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.dictionary.mapper.DictionaryMapper;

/**
 * 数据字典业务处理
 * @author penglong
 *
 */
@Service
@Transactional
public class DictionaryService {
	
	private static final Logger log = LoggerFactory.getLogger(DictionaryService.class);
	
	@Autowired
	DictionaryMapper dm;
	
	public String findMailAddress(String memberMail)
	{
		String mail = "";
		try
		{
			mail = dm.findMailAddress(memberMail.substring(memberMail.indexOf("@")));
		}
		catch(Exception ex)
		{
			log.error("find mail address",ex);
		}
		return mail != null ? mail : "";
	}
}
