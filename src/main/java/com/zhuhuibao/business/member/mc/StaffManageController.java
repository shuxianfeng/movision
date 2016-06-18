package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BaseException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心员工管理
 * @author cxx
 * @since 16/2/25.
 */
@RestController
@RequestMapping("/rest/member/mc/user")
public class StaffManageController {
	private static final Logger log = LoggerFactory.getLogger(StaffManageController.class);

	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "新建员工", notes = "新建员工", response = Response.class)
	@RequestMapping(value = "add_member", method = RequestMethod.POST)
	public Response addMember(@RequestParam String account,@RequestParam String workType,@RequestParam String enterpriseLinkman)  {
		Response result = new Response();

		Long memberId = ShiroUtil.getCreateID();
		if(memberId !=null){
			Member member = memberService.findMemById(String.valueOf(memberId));
			if(account.contains("@")){
				member.setEmail(account);
				member.setMobile("");
			}else{
				member.setMobile(account);
				member.setEmail("");
			}
			member.setWorkType(workType);
			member.setEnterpriseLinkman(enterpriseLinkman);

			String md5Pwd = new Md5Hash("123456",null,2).toString();
			member.setPassword(md5Pwd);

			member.setEnterpriseEmployeeParentId(String.valueOf(memberId));
			//先判断账号是否已经存在
			Member mem = memberService.findMember(member);
			if(mem==null){
				memberService.addMember(member);
			}else{
				throw new BaseException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
			}
		}else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
		return result;
	}

	@ApiOperation(value = "修改员工", notes = "修改员工", response = Response.class)
	@RequestMapping(value = "upd_member", method = RequestMethod.POST)
	public Response updateMember(HttpServletRequest req, Member member)  {
		String account = req.getParameter("account");
		if(account.contains("@")){
			member.setEmail(account);
		}else{
			member.setMobile(account);
		}

		Response result = new Response();
		memberService.updateMemInfo(member);
		return result;

	}

	@ApiOperation(value = "删除员工", notes = "删除员工", response = Response.class)
	@RequestMapping(value = "del_member", method = RequestMethod.POST)
	public Response deleteMember(HttpServletRequest req)  {
		String ids[] = req.getParameterValues("ids");
		Response result = new Response();
		for (String id : ids) {
			Member member = new Member();
			member.setId(id);
			member.setStatus("2");
			memberService.updateMemInfo(member);
		}
		return result;
	}

	@ApiOperation(value = "员工搜索", notes = "员工搜索", response = Response.class)
	@RequestMapping(value = "sel_memberList", method = RequestMethod.GET)
	public Response staffSearch(Member member, String pageNo, String pageSize)  {
		Response response = new Response();
		if(member.getAccount()!=null){
			if(member.getAccount().contains("_")){
				member.setAccount(member.getAccount().replace("_","\\_"));
			}
		}
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<Member> pager = new Paging<Member>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		List list = memberService.findStaffByParentId(pager,member);
		pager.result(list);
		response.setData(pager);
		return response;
	}

	@ApiOperation(value = "员工密码重置", notes = "员工密码重置", response = Response.class)
	@RequestMapping(value = "reset_pwd", method = RequestMethod.POST)
	public Response resetPwd(HttpServletRequest req)  {
		String ids[] = req.getParameterValues("ids");
		Response result = new Response();
		for (String id : ids) {
			Member member = new Member();
			String md5Pwd = new Md5Hash("123456", null, 2).toString();
			member.setPassword(md5Pwd);
			member.setId(id);
			memberService.updateMemInfo(member);
		}

		return result;
	}

	@ApiOperation(value = "员工角色", notes = "员工角色", response = Response.class)
	@RequestMapping(value = "sel_workTypeList", method = RequestMethod.GET)
	public Response workTypeList()  {
		Response result = new Response();
		List<WorkType> workType = memberService.findWorkTypeList();
		result.setData(workType);
		return result;
	}
}
