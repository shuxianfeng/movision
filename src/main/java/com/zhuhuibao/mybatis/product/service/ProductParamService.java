package com.zhuhuibao.mybatis.product.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductParamMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;

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
	 * 产品详情页面查询参数信息
	 * @param Ids
	 * @return
	 */
	public JsonResult queryParam(String paramIDs)
	{
		JsonResult jsonResult = new JsonResult();
		List<ProductParam> paramList = new ArrayList<ProductParam>();
		try
		{
			if(paramIDs != null)
			{
				String[] str_arr = paramIDs.split(",");
				if(str_arr != null && str_arr.length > 0)
				{
					for(String param : str_arr)
					{
						ProductParam productParam = this.queryParamById(Long.parseLong(param));
						if(productParam != null)
						{
							paramList.add(this.queryParamById(Long.parseLong(param)));
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("qurey param error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		jsonResult.setData(paramList);
		return jsonResult;
	}
}
