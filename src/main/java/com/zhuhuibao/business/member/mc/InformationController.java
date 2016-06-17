package com.zhuhuibao.business.member.mc;

import com.mysql.jdbc.StringUtils;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 会员中心公共接口
 * @author cxx
 * @since 16/2/25.
 */
@RestController
@RequestMapping("/rest/member/mc/base")
public class InformationController {
	private static final Logger log = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "判断手机是否绑定", notes = "判断手机是否绑定", response = Response.class)
	@RequestMapping(value = "check_isBind_mobile", method = RequestMethod.POST)
	public Response isBindMobile(HttpServletRequest req) {
		String memId = req.getParameter("id");
		Response result = new Response();
		Member member = memberService.findMemById(memId);
		if(member.getMobile()!=null || !StringUtils.isNullOrEmpty(member.getMobile())){
			result.setCode(200);
			result.setMessage("手机已绑定");
		}else{
			result.setCode(400);
			result.setMessage("手机未绑定");
		}
		return result;
	}

	@ApiOperation(value = "判断邮箱是否绑定", notes = "判断邮箱是否绑定", response = Response.class)
	@RequestMapping(value = "check_isBind_email", method = RequestMethod.POST)
	public Response isBindEmail(HttpServletRequest req)  {
		String memId = req.getParameter("id");
		Response result = new Response();
		Member member = memberService.findMemById(memId);
		if(member.getEmail()!=null || !StringUtils.isNullOrEmpty(member.getEmail())){
			result.setCode(200);
			result.setMessage("邮箱已绑定");
		}else{
			result.setCode(400);
			result.setMessage("邮箱未绑定");
		}
		return result;
	}
}
