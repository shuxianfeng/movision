package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferAskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.OfferPriceMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

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

	@Autowired
	ApiConstants ApiConstants;
	
	/**
	 * 我要报价
	 * @param price
	 * @return
	 */
	public Response addOfferPrice(OfferPrice price)
	{
		Response response = new Response();
		try
		{
			if(price.getBillurl() != null && !price.getBillurl().equals(""))
			{
				String fileUrl = price.getBillurl();
				fileUrl = ApiConstants.getUploadDoc()+ Constants.upload_price_document_url+"/"+fileUrl;
				File file = new File(fileUrl);
				if(file.exists()){
					priceMapper.insertSelective(price);
				}
				else
				{
					response.setCode(400);
					response.setMessage("文件不存在");
					response.setMsgCode(MsgCodeConstant.file_not_exist);
				}
			}
			else if(price.getContent() != null && !price.getContent().equals(""))
			{
				priceMapper.insertSelective(price);
			}
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			response.setCode(MsgCodeConstant.response_status_400);
    		response.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return response;
		}
		return response;
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
     * 报价查询  询价信息+报价信息
     * @param id
     * @return
     */
    public Response queryOfferPriceInfoByID(Long id)
    {
    	Response response = new Response();
		try
		{
			OfferAskPrice price = priceMapper.queryOfferPriceInfoByID(id);
			response.setData(price);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			response.setCode(MsgCodeConstant.response_status_400);
    		response.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return response;
		}
		return response;
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
    public Response queryAllOfferPriceByAskID(Long id)
    {
    	Response response = new Response();
		try
		{
			List<AskPriceSimpleBean> priceList = priceMapper.queryAllOfferPriceByAskID(id);
			response.setData(priceList);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			response.setCode(MsgCodeConstant.response_status_400);
    		response.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return response;
		}
		return response;
    }
    
    /**
     * 查看回复的具体某条报价信息(清单,单一产品)
     * @param id
     * @return
     */
    public Response queryOfferPriceByID(Long id)
    {
    	Response response = new Response();
		try
		{
			OfferPrice price = priceMapper.selectByPrimaryKey(id);
			response.setData(price);
		}
		catch(Exception e)
		{
			log.error("add offer price error!",e);
			response.setCode(MsgCodeConstant.response_status_400);
    		response.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		response.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return response;
		}
		return response;
    }
    
}
