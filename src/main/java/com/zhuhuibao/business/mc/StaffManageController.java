package com.zhuhuibao.business.mc;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BaseException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
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
public class StaffManageController {
	private static final Logger log = LoggerFactory.getLogger(StaffManageController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberMapper memberMapper;

	/**
	 * 新建会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/addMember", method = RequestMethod.POST)
	public Response addMember(HttpServletRequest req, Member member)  {
		String account = req.getParameter("account");
		if(account.contains("@")){
			member.setEmail(account);
		}else{
			member.setMobile(account);
		}
		Response result = new Response();

		String md5Pwd = new Md5Hash("123456",null,2).toString();
		member.setPassword(md5Pwd);
		//先判断账号是否已经存在
		Member mem = memberService.findMember(member);
		if(mem==null){
			memberService.addMember(member);
		}else{
			throw new BaseException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
		}

		return result;
	}

	/**
	 * 修改会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/updateMember", method = RequestMethod.POST)
	public Response updateMember(HttpServletRequest req, Member member)  {
		String account = req.getParameter("account");
		if(account.contains("@")){
			member.setEmail(account);
		}else{
			member.setMobile(account);
		}

		Response result = new Response();
		memberService.updateMember(member);
		return result;

	}

	/**
	 * 禁用会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

/*	@RequestMapping(value = "/rest/disableMember", method = RequestMethod.POST)
	public void disableMember(HttpServletRequest req, HttpServletResponse response,Member member) throws IOException {
		Response result = new Response();
		int isDisable = memberService.disableMember(member);
		if(isDisable==0){
			result.setCode(400);
			result.setMessage("禁用失败");
		}else{
			result.setCode(200);
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}*/

	/**
	 * 删除会员
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/deleteMember", method = RequestMethod.POST)
	public Response deleteMember(HttpServletRequest req)  {
		String ids[] = req.getParameterValues("ids");
		Response result = new Response();
		for (String id : ids) {
			memberService.deleteMember(id);
		}
		return result;
	}

	/**
	 * 员工搜索
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/staffSearch", method = RequestMethod.GET)
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

	/**
	 * 员工搜索size
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/staffSearchSize", method = RequestMethod.GET)
	public Response staffSearchSize(Member member)  {
		Response result = new Response();
		List<Member> memberList = memberMapper.findStaffByParentId(member);
		Map map = new HashMap();
		map.put("size",memberList.size());
		map.put("leftSize",30-memberList.size());
		result.setCode(200);
		result.setData(map);

		return result;
	}

	/**
	 * 员工密码重置
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/resetPwd", method = RequestMethod.POST)
	public Response resetPwd(HttpServletRequest req)  {
		String ids[] = req.getParameterValues("ids");
		Response result = new Response();
		for (String id : ids) {
			Member member = new Member();
			String md5Pwd = new Md5Hash("123456", null, 2).toString();
			member.setPassword(md5Pwd);
			member.setId(Long.parseLong(id));
			memberService.resetPwd(member);
		}

		return result;
	}
}
