package com.zhuhuibao.business.member.base;

import java.io.IOException;
import java.util.Map;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.fsearch.service.impl.MembersService;

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
	
	@RequestMapping(value={"/rest/searchContractors","/rest/member/site/sel_contractors"}, method = RequestMethod.GET)
	public Response searchContractors(ContractorSearchSpec spec) throws IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		Response response = new Response();
		response.setCode(200);
		Map<String, Object> ret;
		try{
			ret = membersService.searchContractors(spec);
			response.setMsgCode(1);
			response.setMessage("OK!");
			response.setData(ret);
		}
		catch (Exception e) {
			response.setMsgCode(0);
			response.setMessage("search error!");
		}

		return response;
	}
	
	@RequestMapping(value={"/rest/searchSuppliers","/rest/membe/site/sel_suppliers"}, method = RequestMethod.GET)
	public Response searchSuppliers(SupplierSearchSpec spec) throws IOException
	{
		if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
			spec.setLimit(12);
		}
		Response response = new Response();
		response.setCode(200);
		Map<String, Object> ret;
		try{
			ret = membersService.searchSuppliers(spec);
			response.setMsgCode(1);
			response.setMessage("OK!");
			response.setData(ret);
		}
		catch (Exception e) {
			response.setMsgCode(0);
			response.setMessage("search error!");
		}

		return response;
	}
	
}
