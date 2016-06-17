package com.zhuhuibao.mybatis.product.service;

import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductParamService {
	
	private static final Logger log = LoggerFactory.getLogger(ProductParamService.class);
	
	@Autowired
	ProductParamMapper paramMapper;
	
	/**
	 * 插入参数
	 * @param product  产品表
	 * @return
	 */
	public Map<String,Long> insertParam(ProductWithBLOBs product)
	{
		Map<String,Long> paramMap = new HashMap<String,Long>(); 
		log.info("insert param ");
		try
		{
			List<ProductParam> params = product.getParams();
			if(params != null && !params.isEmpty())
			{
				for(int i =0;i<params.size();i++)
				{
					ProductParam param = params.get(i);
					param.setCreateId(product.getCreateid());
					paramMapper.insert(param);
					log.info("paramId == "+param.getId());
					paramMap.put(param.getPname(), param.getId());
				}
			}
		}
		catch(Exception ex)
		{
			log.error("insert params error!",ex);
		}
		return paramMap;
	}
	
	/**
	 * 查询参数信息根据ID
	 * @param paramId
	 * @return
	 */
	public ProductParam queryParamById(Long paramId)
	{
		ProductParam param = null;
		try
		{
			param = paramMapper.selectByPrimaryKey(paramId);
		}
		catch(Exception e)
		{
			log.error("qurey param by id error!",e);
			return param;
		}
		return param;
	}
	
	/**
	 * 查询参数查询所有产品 
	 * @param paramId
	 * @return
	 */
	public List<ProductParam> selectParamByIds(List<Integer> list)
	{
		List<ProductParam> params = null;
		try
		{
			params = paramMapper.selectParamByIds(list);
		}
		catch(Exception e)
		{
			log.error("qurey param by id error!",e);
			return params;
		}
		return params;
	}
}
