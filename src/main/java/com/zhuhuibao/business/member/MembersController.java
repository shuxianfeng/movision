package com.zhuhuibao.business.member;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.service.impl.MembersService;
import com.zhuhuibao.pojo.ContractorSearchSpec;
import com.zhuhuibao.pojo.SupplierSearchSpec;

/**
 * 会员控制
 * @author caijl@20160414
 *
 */
@Controller
public class MembersController {
	private static final Logger log = LoggerFactory.getLogger(MembersController.class);
	
	@Autowired
	MembersService membersService;
	
	@RequestMapping(value="/rest/searchContractors", method = RequestMethod.GET)
	@ResponseBody
	public void searchContractors(HttpServletRequest req,HttpServletResponse response,ContractorSearchSpec spec) throws JsonGenerationException, JsonMappingException, IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		Map<String, Object> ret = null;
		try{
			ret = membersService.searchContractors(spec);
		}
		catch (Exception e) {
			jsonResult.setMsgCode(0);
			jsonResult.setMessage("search error!");
		}
		
		jsonResult.setMsgCode(1);
		jsonResult.setMessage("OK!");
		jsonResult.setData(ret);

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	@RequestMapping(value="/rest/searchSuppliers", method = RequestMethod.GET)
	@ResponseBody
	public void searchSuppliers(HttpServletRequest req,HttpServletResponse response,SupplierSearchSpec spec) throws JsonGenerationException, JsonMappingException, IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		JsonResult jsonResult = new JsonResult();
		jsonResult.setCode(200);
		Map<String, Object> ret = null;
		try{
			ret = membersService.searchSuppliers(spec);
		}
		catch (Exception e) {
			jsonResult.setMsgCode(0);
			jsonResult.setMessage("search error!");
		}
		
		jsonResult.setMsgCode(1);
		jsonResult.setMessage("OK!");
		jsonResult.setData(ret);

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
}
