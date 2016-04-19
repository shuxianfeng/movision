package com.zhuhuibao.mybatis.memCenter.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.AskPriceBean;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferAskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.OfferPriceMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 报价业务处理类
 * @author PengLong
 *
 */
@Transactional
@Service
public class OfferPriceService {
	private static final Logger log = LoggerFactory.getLogger(OfferPriceService.class);
	
	@Resource
	OfferPriceMapper priceMapper;
	
	@Autowired
    AskPriceMapper askPriceMapper;
	
	/**
	 * 我要报价
	 * @param offerPrice
	 * @return
	 */
	public JsonResult addOfferPrice(OfferPrice offerPrice)
	{
		JsonResult jsonResult = new JsonResult();
		try
		{
			int result = priceMapper.insertSelective(offerPrice);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		return jsonResult;
	}
	
	/**
     * 查询我的报价中的询价信息
     * @param pager    分页属性
     * @return   product 报价信息
     */
    public List<AskPriceSimpleBean> findAllAskingPriceInfo(Paging<AskPriceSimpleBean> pager,AskPrice price) {
        log.debug("分页询价需求");
        return priceMapper.findAllAskingPriceInfo(pager.getRowBounds(),price);
    }
    
    /**
     * 查询我的报价中的询价信息
     * @param pager    分页属性
     * @return   product 报价信息
     */
    public List<AskPriceSimpleBean> findAllOfferedPriceInfo(Paging<AskPriceSimpleBean> pager,Map<String,String> priceMap) {
        log.debug("分页查询我已报价的信息");
        return priceMapper.findAllOfferedPriceInfo(pager.getRowBounds(),priceMap);
    }
    
    /**
     * 查询报价信息根据ID
     * @param id
     * @return
     */
    public JsonResult queryOfferPriceInfoByID(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
		try
		{
			OfferPrice price = priceMapper.selectByPrimaryKey(id);
			jsonResult.setData(price);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		return jsonResult;
    }
    
    /**
     * 下载清单
     * @param id
     * @return
     */
    public String downloadBill(Long id,String type)
    {
    	String fileurl = "";
		try
		{
			if(type.equals("2"))
			{
				OfferPrice price = priceMapper.selectByPrimaryKey(id);
				if(price != null && price.getBillurl() != null)
				{
					fileurl = price.getBillurl();
				}
			}
			else if(type.equals("1"))
			{
				AskPriceBean askPrice = askPriceMapper.queryAskPriceByID(String.valueOf(id));
				if(askPrice != null && askPrice.getBillurl() != null)
				{
					fileurl = askPrice.getBillurl();
				}
			}
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
		}
		return fileurl;
    }
    
    /**
     * 查看某条询价信息的所有报价信息
     * @param id
     * @return
     */
    public JsonResult queryAllOfferPriceByAskID(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
		try
		{
			List<AskPriceSimpleBean> priceList = priceMapper.queryAllOfferPriceByAskID(id);
			jsonResult.setData(priceList);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		return jsonResult;
    }
    
    /**
     * 查看回复的具体某条报价信息(清单,单一产品)
     * @param id
     * @return
     */
    public JsonResult queryOfferPriceByID(Long id)
    {
    	JsonResult jsonResult = new JsonResult();
		try
		{
			OfferAskPrice price = priceMapper.queryOfferPriceByID(id);
			jsonResult.setData(price);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		return jsonResult;
    }
    
}
