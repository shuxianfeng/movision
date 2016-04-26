package com.zhuhuibao.business.memCenter.PriceManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

@Controller
public class OfferPriceController {
	
	private static final Logger log = LoggerFactory.getLogger(OfferPriceController.class);
	
	@Resource
	private OfferPriceService offerService;
	
	@Autowired
    ApiConstants ApiConstants;
	
	@RequestMapping(value="/rest/price/addOfferPrice", method = RequestMethod.POST)
	public void addOfferPrice(HttpServletRequest req,HttpServletResponse response,OfferPrice price) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("add offer price");
		JsonResult jsonResult = new JsonResult();
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(session != null)
        {
        	ShiroUser principal = (ShiroUser)session.getAttribute("member");
        	price.setCreateid(new Long(principal.getId()));
			jsonResult = offerService.addOfferPrice(price);
        }
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryAskingPriceInfo")
	@ResponseBody
	public void queryAskingPriceInfo(HttpServletRequest req,HttpServletResponse response,AskPrice price,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		Long createID= ShiroUtil.getCreateID();
		if(createID != null) {
			price.setCreateid(String.valueOf(createID));
			if (StringUtils.isEmpty(pageNo)) {
				pageNo = "1";
			}
			if (StringUtils.isEmpty(pageSize)) {
				pageSize = "10";
			}
			if(price.getTitle() != null && !price.getTitle().equals(""))
			{
				price.setTitle(price.getTitle().replace("_","\\_"));
			}
			Paging<AskPriceSimpleBean> pager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			List<AskPriceSimpleBean> priceList = offerService.findAllAskingPriceInfo(pager, price);
			pager.result(priceList);
			jsonResult.setData(pager);
		}
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryOfferedPriceInfo")
	@ResponseBody
	public void queryOfferedPriceInfo(HttpServletRequest req,HttpServletResponse response,String title,String startDate,String endDate,String pageNo,String pageSize) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		Long createID= ShiroUtil.getCreateID();
		if(createID != null ) {
			if (StringUtils.isEmpty(pageNo)) {
				pageNo = "1";
			}
			if (StringUtils.isEmpty(pageSize)) {
				pageSize = "10";
			}
			Paging<AskPriceSimpleBean> pager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			Map<String, String> priceMap = new HashMap<String, String>();
			if(title != null && !title.equals(""))
			{
				priceMap.put("title", title.replace("_","\\_"));
			}
			priceMap.put("startDate", startDate);
			priceMap.put("endDate", endDate);
			priceMap.put("createid", String.valueOf(createID));
			List<AskPriceSimpleBean> priceList = offerService.findAllOfferedPriceInfo(pager, priceMap);
			pager.result(priceList);
			jsonResult.setData(pager);
		}
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryOfferPriceInfoByID", method = RequestMethod.GET)
	public void queryOfferPriceInfoByID(HttpServletRequest req,HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query offer priece info by id ");
		JsonResult jsonResult = offerService.queryOfferPriceInfoByID(id);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/downloadBill", method = RequestMethod.GET)
	public void downloadBill(HttpServletRequest req,HttpServletResponse response,Long id,String type) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query offer priece info by id ");
		try {
			String fileurl = offerService.downloadBill(id, type);
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control",
					"no-store, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
			response.setContentType("application/octet-stream");
			fileurl = ApiConstants.getUploadDoc() + Constant.upload_price_document_url + "/" + fileurl;
			File file = new File(fileurl);
			if (file.exists()) {   //如果文件存在
				FileInputStream inputStream = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				int length = inputStream.read(data);
				inputStream.close();
				ServletOutputStream stream = response.getOutputStream();
				stream.write(data);
				stream.flush();
				stream.close();
			}
		}
		catch(Exception e)
		{
			log.error("download bill error! ",e);
		}
	}
	
	@RequestMapping(value="/rest/price/queryAllOfferPriceByAskID", method = RequestMethod.GET)
	public void queryAllOfferPriceByAskID(HttpServletRequest req,HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query all offer priece by askid ");
		JsonResult jsonResult = offerService.queryAllOfferPriceByAskID(id);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/price/queryOfferPriceByID", method = RequestMethod.GET)
	public void queryOfferPriceByID(HttpServletRequest req,HttpServletResponse response,Long id) throws JsonGenerationException, JsonMappingException, IOException
	{
		log.info("query offer priece info by id ");
		JsonResult jsonResult = offerService.queryOfferPriceByID(id);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
}
