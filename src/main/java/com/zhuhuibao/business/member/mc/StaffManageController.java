package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BaseException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员中心员工管理
 * @author cxx
 * @since 16/2/25.
 */
@RestController
@RequestMapping("/rest/member/mc/company")
public class StaffManageController {
	private static final Logger log = LoggerFactory.getLogger(StaffManageController.class);

	@Autowired
	private MemberService memberService;

	@ApiOperation(value = "新建员工", notes = "新建员工", response = Response.class)
	@RequestMapping(value = "add_member", method = RequestMethod.POST)
	public Response addMember(@RequestParam String account,@RequestParam Integer workType,@RequestParam String enterpriseLinkman) throws Exception {
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
			member.setEnterpriseLMDep("");
			member.setFixedTelephone("");
			member.setFixedMobile("");
			member.setQQ("");
			member.setSex(null);

			String md5Pwd = new Md5Hash("123456",null,2).toString();
			member.setPassword(md5Pwd);

			member.setEnterpriseEmployeeParentId(String.valueOf(memberId));
			//先判断账号是否已经存在
			Member mem = memberService.findMember(member);
			if(mem==null){
				memberService.addMember(member);
			}else{
				throw new BusinessException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
			}
		}else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
		return result;
	}

	@ApiOperation(value = "修改员工", notes = "修改员工", response = Response.class)
	@RequestMapping(value = "upd_member", method = RequestMethod.POST)
	public Response updateMember(@RequestParam String account,
								 @RequestParam Integer workType,
								 @RequestParam String enterpriseLinkman,
								 @RequestParam String id)  {
		Member member = new Member();
		if(account.contains("@")){
			member.setEmail(account);
		}else{
			member.setMobile(account);
		}
		member.setId(id);
		member.setWorkType(workType);
		member.setEnterpriseLinkman(enterpriseLinkman);
		Response result = new Response();
		memberService.updateMemInfo(member);
		return result;

	}

	@ApiOperation(value = "删除员工", notes = "删除员工", response = Response.class)
	@RequestMapping(value = "del_member", method = RequestMethod.POST)
	public Response deleteMember(@ApiParam(value = "员工ids,逗号隔开") @RequestParam String ids)  {
		String[] idList = ids.split(",");
		Response result = new Response();
		for (String id : idList) {
			Member member = new Member();
			member.setId(id);
			member.setStatus("2");	//注销
			memberService.updateMemInfo(member);
		}
		return result;
	}

	@ApiOperation(value = "员工搜索", notes = "员工搜索", response = Response.class)
	@RequestMapping(value = "sel_memberList", method = RequestMethod.GET)
	public Response staffSearch(@RequestParam(required = false) String account,
								@RequestParam(required = false,defaultValue = "1") String pageNo,
								@RequestParam(required = false,defaultValue = "10") String pageSize)  {
		Response response = new Response();

		Map<String, Object> map = new HashMap<>();
		if(account!=null){
			if(account.contains("_")){
				account = account.replace("_","\\_");
			}
		}
		map.put("account",account);
		Long memberId = ShiroUtil.getCreateID();
		if(memberId!=null){
			map.put("enterpriseEmployeeParentId",String.valueOf(memberId));
			Paging<Member> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
			List list = memberService.findStaffByParentId(pager,map);
			pager.result(list);
			response.setData(pager);
		}else {
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
		return response;
	}

	@ApiOperation(value = "员工密码重置", notes = "员工密码重置", response = Response.class)
	@RequestMapping(value = "reset_pwd", method = RequestMethod.POST)
	public Response resetPwd(@ApiParam(value = "员工ids,逗号隔开") @RequestParam String ids)  {
		Response result = new Response();
		String[] idList = ids.split(",");
		for (String id : idList) {
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
