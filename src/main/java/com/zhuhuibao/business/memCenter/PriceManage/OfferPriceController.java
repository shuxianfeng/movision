package com.zhuhuibao.business.memCenter.PriceManage;

import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

@Controller
public class OfferPriceController {
	
	private static final Logger log = LoggerFactory.getLogger(OfferPriceController.class);
	
	@Resource
	private OfferPriceService offerService;
	
	@RequestMapping(value="/rest/price/addOfferPrice", method = RequestMethod.POST)
	public void addOfferPrice(HttpServletRequest req,HttpServletResponse response,OfferPrice price) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("add offer price");
		JsonResult jsonResult = offerService.addOfferPrice(price);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryAskingPriceInfo")
	@ResponseBody
	public void queryAskingPriceInfo(HttpServletRequest req,HttpServletResponse response,AskPrice price,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<AskPriceSimpleBean> pager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<AskPriceSimpleBean>  priceList = offerService.findAllAskingPriceInfo(pager, price);
        pager.result(priceList);
        jsonResult.setData(pager);
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryOfferedPriceInfo")
	@ResponseBody
	public void queryOfferedPriceInfo(HttpServletRequest req,HttpServletResponse response,String title,String createid,
			String startDate,String endDate,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<AskPriceSimpleBean> pager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Map<String,String> priceMap = new HashMap<String,String>();
        priceMap.put("title",title);
        priceMap.put("startDate",startDate);
        priceMap.put("endDate",endDate);
        priceMap.put("createid",createid);
        List<AskPriceSimpleBean>  priceList = offerService.findAllOfferedPriceInfo(pager, priceMap);
        pager.result(priceList);
        jsonResult.setData(pager);
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryOfferPriceInfoByID", method = RequestMethod.GET)
	public void queryOfferPriceInfoByID(HttpServletRequest req,HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query offer priece info by id ");
		JsonResult jsonResult = offerService.queryOfferPriceInfoByID(id);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/downloadBill", method = RequestMethod.GET)
	public void downloadBill(HttpServletRequest req,HttpServletResponse response,Long id,String type) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query offer priece info by id ");
		JsonResult jsonResult = offerService.downloadBill(id, type);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
}
