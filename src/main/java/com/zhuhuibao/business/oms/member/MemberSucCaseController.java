package com.zhuhuibao.business.oms.member;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.oms.entity.MemberSucCase;
import com.zhuhuibao.mybatis.oms.service.MemberSucCaseService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 成功案例
 * @author gmli
 * @since 2016.7.18
 *
 */
@RestController
@RequestMapping("/rest/member/oms/suc/")
@Api(value = "MemberSucCaseController",description = "运营平台成功案例")
public class MemberSucCaseController {
	private static final Logger log = LoggerFactory.getLogger(MemberSucCaseController.class);
	@Autowired
	private MemberSucCaseService memberSucCaseService;
	
	@ApiOperation(value="查询成功案例（分页）",notes="查询成功案例（分页）",response = Response.class)
	@RequestMapping(value="sel_sucCaseList",method = RequestMethod.GET)
	public Response querySucCaseList(
			@RequestParam(required = false) String title,
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String pageNo,
			@RequestParam(required = false) String pageSize) {
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging pager = new Paging(
				Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		List result = memberSucCaseService.findAllSucCaseList(pager, title,status);
		pager.result(result);
	    return new Response(pager);
	}
	
	@ApiOperation(value = "查询案例详情", notes = "查询案例详情", response = Response.class)
	@RequestMapping(value = "sel_memberSucCase", method = RequestMethod.GET)
	public Response queryMemberSucCase(@ApiParam(value = "案例ID") @RequestParam String id)  {
		Response result = new Response();
		MemberSucCase memberSucCase = memberSucCaseService.queryMemberSucCase(id);
		result.setData(memberSucCase);
		return result;
	}
	
	@RequestMapping(value = "upd_suc_case", method = RequestMethod.POST)
	@ApiOperation(value="成功案例修改",notes="成功案例修改",response = Response.class)
	public Response updateSucCase(
			@ApiParam(value = "成功案例信息") @ModelAttribute(value = "memberSucCase") MemberSucCase memberSucCase)  {
		Response result = new Response();
		memberSucCaseService.updateSucCase(memberSucCase);
		return result;
	}

}
