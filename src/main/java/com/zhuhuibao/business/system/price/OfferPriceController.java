package com.zhuhuibao.business.system.price;

import java.io.IOException;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.utils.file.FileUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 报价
 * @author  pl
 * date : 2016
 */
@RestController
//@RequestMapping("/rest/price")
@Api(value="OfferPrice", description="报价")
public class OfferPriceController {
	
	private static final Logger log = LoggerFactory.getLogger(OfferPriceController.class);
	
	@Resource
	private OfferPriceService offerService;
	
	@Autowired
    ApiConstants ApiConstants;

	@Autowired
	FileUtil fileUtil;

	@ApiOperation(value="我要报价(清单和单一产品)",notes="我要报价(清单和单一产品)",response = Response.class)
	@RequestMapping(value={"rest/price/addOfferPrice","rest/system/mc/quote/add_offerPrice"}, method = RequestMethod.POST)
	public Response addOfferPrice(OfferPrice price) throws IOException
	{
		log.info("add offer price");
		Response response = new Response();
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(session != null)
        {
        	ShiroUser principal = (ShiroUser)session.getAttribute("member");
        	price.setCreateid(new Long(principal.getId()));
			response = offerService.addOfferPrice(price);
        }
		return response;
	}

	@ApiOperation(value="询价需求功能：查询所有正在询价中的信息（分页）",notes="询价需求功能：查询所有正在询价中的信息（分页）",response = Response.class)
	@RequestMapping(value={"rest/price/queryAskingPriceInfo","rest/system/mc/quote/sel_askingPriceInfo"},method = RequestMethod.GET)
	public Response queryAskingPriceInfo(AskPrice price,
										 @RequestParam(required = false) String pageNo,
										 @RequestParam(required = false) String pageSize) throws IOException
	{
		Response response = new Response();
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
			response.setData(pager);
		}
        return response;
	}

	@ApiOperation(value="我的报价：根据条件查询自己所有报价信息（分页）",notes="我的报价：根据条件查询自己所有报价信息（分页）",response = Response.class)
	@RequestMapping(value={"rest/price/queryOfferedPriceInfo","rest/system/mc/quote/sel_offeredPriceInfo"},method = RequestMethod.GET)
	public Response queryOfferedPriceInfo(@RequestParam(required = false) String title,
										  @RequestParam(required = false) String startDate,
										  @RequestParam(required = false) String endDate,
										  @RequestParam(required = false) String pageNo,
										  @RequestParam(required = false) String pageSize) throws IOException
	{
		Response response = new Response();
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
			response.setData(pager);
		}
        return response;
	}

	@ApiOperation(value="公开，定向，单一产品报价查询",notes="公开，定向，单一产品报价查询",response = Response.class)
	@RequestMapping(value={"rest/price/queryOfferPriceInfoByID","rest/system/mc/quote/sel_offerPriceInfoByID"}, method = RequestMethod.GET)
	public Response queryOfferPriceInfoByID(@RequestParam Long id) throws IOException
	{
		log.debug("query offer priece info by id ");
		Response response = offerService.queryOfferPriceInfoByID(id);
		return response;
	}

	@ApiOperation(value="下载报价单，询价单",notes="下载报价单，询价单",response = Response.class)
	@RequestMapping(value={"rest/price/downloadBill","rest/system/mc/quote/dl_bill"}, method = RequestMethod.GET)
	public Response downloadBill(HttpServletResponse response, @RequestParam Long id, @RequestParam String type) throws IOException
	{
		Response jsonResult = new Response();
		log.debug("query offer priece info by id ");
		try {
			String fileurl = offerService.downloadBill(id, type);
			response.setDateHeader("Expires", 0);
			response.setHeader("Cache-Control",
					"no-store, no-cache, must-revalidate");
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
			response.setContentType("application/octet-stream");
//			fileurl = ApiConstants.getUploadDoc() + Constants.upload_price_document_url + "/" + fileurl;
			jsonResult = fileUtil.downloadObject(response, fileurl,"doc","price");
		}
		catch(Exception e)
		{
			log.error("download bill error! ",e);
		}
		return jsonResult;
	}

	@ApiOperation(value="查看某条询价信息的所有报价信息",notes="查看某条询价信息的所有报价信息",response = Response.class)
	@RequestMapping(value={"rest/price/queryAllOfferPriceByAskID","rest/system/mc/quote/sel_allOfferPriceByAskID"}, method = RequestMethod.GET)
	public Response queryAllOfferPriceByAskID(@RequestParam Long id) throws IOException
	{
		log.debug("query all offer priece by askid ");
		return offerService.queryAllOfferPriceByAskID(id);
	}

	@ApiOperation(value="查看回复的具体某条报价信息(清单,单一产品)",notes="查看回复的具体某条报价信息(清单,单一产品)",response = Response.class)
	@RequestMapping(value={"rest/price/queryOfferPriceByID","rest/system/mc/quote/sel_offerPriceByID"}, method = RequestMethod.GET)
	public Response queryOfferPriceByID(@RequestParam Long id) throws IOException
	{
		log.debug("query offer priece info by id ");
		return offerService.queryOfferPriceByID(id);
	}
}
