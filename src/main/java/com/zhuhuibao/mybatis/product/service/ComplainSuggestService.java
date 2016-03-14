package com.zhuhuibao.mybatis.product.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.product.entity.ComplainSuggest;
import com.zhuhuibao.mybatis.product.mapper.ComplainSuggestMapper;

@Service
@Transactional
public class ComplainSuggestService {
	private static final Logger log = LoggerFactory.getLogger(ComplainSuggestService.class);
	
	@Autowired
	ComplainSuggestMapper suggestMapper;
	
	public int insert(ComplainSuggest cs)
	{
		log.info("insert complain suggest");
		int result =0;
		try
		{
			result = suggestMapper.insert(cs);
		}
		catch(Exception e)
		{
			log.error("insert complain suggest error!",e);
		}
		return result;
	}
}
