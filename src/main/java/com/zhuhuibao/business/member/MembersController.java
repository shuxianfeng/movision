package com.zhuhuibao.business.member;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.service.impl.MembersService;
import com.zhuhuibao.pojo.ContractorSearchSpec;
import com.zhuhuibao.pojo.SupplierSearchSpec;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员控制
 * @author caijl@20160414
 *
 */
@RestController
public class MembersController {
	private static final Logger log = LoggerFactory.getLogger(MembersController.class);
	
	@Autowired
	MembersService membersService;
	
	@RequestMapping(value="/rest/searchContractors", method = RequestMethod.GET)
	public JsonResult searchContractors(ContractorSearchSpec spec) throws IOException
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

		return jsonResult;
	}
	
	@RequestMapping(value="/rest/searchSuppliers", method = RequestMethod.GET)
	public JsonResult searchSuppliers(SupplierSearchSpec spec) throws IOException
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

		return jsonResult;
	}
	
}
