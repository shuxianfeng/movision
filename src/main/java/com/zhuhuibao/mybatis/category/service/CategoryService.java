package com.zhuhuibao.mybatis.category.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;

@Service("cs")
@Transactional
public class CategoryService {
	
	private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
	
	@Autowired
	CategoryMapper categoryMapper;
	
	public ResultBean getFcateByScate(int scateid)
	{
		ResultBean fcate = null;
		try
		{
			List<ResultBean> lfcate = categoryMapper.getFcateByScate(scateid);
			if(null!=lfcate && lfcate.size()>0){
				fcate = lfcate.get(0);
			}
		}
		catch(Exception ex)
		{
			log.error("getFcateByScate",ex);
		}
		return fcate;
	}
}
