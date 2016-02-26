package com.zhuhuibao.business.memCenter.memManage.controller;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jianglz
 * @since 15/12/12.
 */
@RestController
public class InformationController {
	private static final Logger log = LoggerFactory
			.getLogger(InformationController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 个人基本信息完善
	 * @param req
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/memBasicInfo", method = RequestMethod.POST)
	public void memBasicInfo(HttpServletRequest req, HttpServletResponse response, Member member, Model model) throws IOException {
		JsonResult result = new JsonResult();
		if(member.getPersonRealName()==null || StringUtils.isEmpty(member.getPersonRealName())){
			result.setCode(400);
			result.setMessage("姓名为必填项");
		}else if(member.getPersonSex()==null){
			result.setCode(400);
			result.setMessage("性别为必填项");
		}else if(member.getPersonPosition()==null){
			result.setCode(400);
			result.setMessage("个人工作类别职位为必填项");
		}else if(member.getPersonProvince() == null || member.getPersonCity() == null || member.getPersonArea() == null){
			result.setCode(400);
			result.setMessage("省市区/县为必填项");
		}else if(member.getPersonAddress() == null){
			result.setCode(400);
			result.setMessage("地址为必填项");
		}else{
			int isUpdate = memberService.updateMemBasicInfo(member);
			if (isUpdate == 0) {
				result.setCode(400);
				result.setMessage("保存失败");
			} else {
				result.setCode(200);
			}
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}



}
