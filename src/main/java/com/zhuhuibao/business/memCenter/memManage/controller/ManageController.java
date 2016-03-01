package com.zhuhuibao.business.memCenter.memManage.controller;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author cxx
 * @since 16/2/25.
 */
@RestController
public class ManageController {
	private static final Logger log = LoggerFactory
			.getLogger(ManageController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CertificateMapper certificateMapper;

	/**
	 * 新建会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/addMember", method = RequestMethod.POST)
	public void addMember(HttpServletRequest req, HttpServletResponse response, Member member) throws IOException {
		JsonResult result = new JsonResult();
		int isAdd = memberService.addMember(member);
		if(isAdd==0){
			result.setCode(400);
			result.setMessage("新增失败");
		}else{
			result.setCode(200);
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 修改会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/updateMember", method = RequestMethod.POST)
	public void updateMember(HttpServletRequest req, HttpServletResponse response, Member member) throws IOException {
		JsonResult result = new JsonResult();
		int isUpdate = memberService.updateMember(member);
		if(isUpdate==0){
			result.setCode(400);
			result.setMessage("修改失败");
		}else{
			result.setCode(200);
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 禁用会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/disableMember", method = RequestMethod.POST)
	public void disableMember(HttpServletRequest req, HttpServletResponse response,Member member) throws IOException {
		JsonResult result = new JsonResult();
		int isDisable = memberService.disableMember(member);
		if(isDisable==0){
			result.setCode(400);
			result.setMessage("禁用失败");
		}else{
			result.setCode(200);
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 删除会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/deleteMember", method = RequestMethod.POST)
	public void deleteMember(HttpServletRequest req, HttpServletResponse response,Member member) throws IOException {
		JsonResult result = new JsonResult();
		int isdelete = memberService.deleteMember(member);
		if(isdelete==0){
			result.setCode(400);
			result.setMessage("删除失败");
		}else{
			result.setCode(200);
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}
}
