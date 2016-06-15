package com.zhuhuibao.business.member.mc;

import com.mysql.jdbc.StringUtils;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 会员中心资料维护
 * @author cxx
 * @since 16/2/25.
 */
@RestController
public class InformationController {
	private static final Logger log = LoggerFactory.getLogger(InformationController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	ApiConstants ApiConstants;

	@Autowired
	UploadService uploadService;
	/**
	 * 根据ID查询会员所有信息
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/info","/rest/member/mc/base/sel_mem_info"}, method = RequestMethod.GET)
	public Response info(HttpServletRequest req) {
		Response result = new Response();
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		result.setCode(200);
		result.setData(member);
		return result;
	}


	/**
	 * 会员资料保存
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/detailInfo","/rest/member/mc/base/upd_mem_info"}, method = RequestMethod.POST)
	public Response detailInfo(Member member)  {
		Response result = new Response();
		memberService.updateMemInfo(member);
		Member loginMember = memberService.findMemById(member.getId().toString());
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		if (session != null) {
			ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
			principal.setStatus(loginMember.getStatus());
			session.setAttribute("member", principal);
		}
		return result;
	}

	/**
	 * 会员状态审核
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/updateStatus","/rest/member/mc/base/upd_mem_status"}, method = RequestMethod.POST)
	public Response updateStatus(Member member)  {
		Response result = new Response();
		memberService.updateStatus(member);
		return result;
	}

	/**
	 * 资质类型
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/certificateList","/rest/member/mc/company/sel_certificateList"}, method = RequestMethod.GET)
	public Response certificateList(HttpServletRequest req)  {
		Response response = new Response();
		String type = req.getParameter("type");
		List list = memberService.findCertificateList(type);
		response.setData(list);
		return response;
	}

	/**
	 * 工作类别
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/member/mc/individual/sel_workTypeList", method = RequestMethod.GET)
	public Response workTypeList()  {
		Response result = new Response();
		List<WorkType> workType = memberService.findIndividualWorkTypeList();
		result.setData(workType);
		return result;
	}

	/**
	 * 企业性质
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/enterpriseTypeList","/rest/member/mc/company/sel_enterpriseTypeList"}, method = RequestMethod.GET)
	public Response enterpriseTypeList()  {
		Response result = new Response();
		List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
		result.setData(enterpriseType);
		return result;
	}

	/**
	 * 企业身份
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/identityList","/rest/member/mc/company/sel_identityList"}, method = RequestMethod.GET)
	public Response identityList()  {
		Response result = new Response();
		List<Identity> identity = memberService.findIdentityList();
		result.setData(identity);
		return result;
	}


	/**
	 * 人员规模
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/employeeSizeList","/rest/member/mc/company/sel_employeeSizeList"}, method = RequestMethod.GET)
	public Response employeeSizeList()  {
		Response result = new Response();
		List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
		result.setData(employeeSizeList);
		return result;
	}

	/**
	 * 查询省
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = {"/rest/searchProvince","/rest/member/mc/base/sel_province"}, method = RequestMethod.GET)
	public Response searchProvince()  {
		Response result = new Response();
		List<ResultBean> province = memberService.findProvince();
		result.setData(province);
		return result;
	}

	/**
	 * 根据省Code查询市
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/searchCity","/rest/member/mc/base/sel_city"}, method = RequestMethod.GET)
	public Response searchCity(String provincecode)  {
		Response result = new Response();
		List<ResultBean> city = memberService.findCity(provincecode);
		result.setData(city);
		return result;
	}

	/**
	 * 根据市Code查询县区
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/searchArea","/rest/member/mc/base/sel_area"}, method = RequestMethod.GET)
	public Response searchArea(String cityCode)  {
		Response result = new Response();
		List<ResultBean> area = memberService.findArea(cityCode);
		result.setData(area);
		return result;

	}

	/**
	 * 判断是否绑定手机
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/isBindMobile","/rest/member/mc/base/check_isBind_mobile"}, method = RequestMethod.POST)
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

	/**
	 * 判断是否绑定邮箱
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/isBindEmail","/rest/member/mc/base/check_isBind_email"}, method = RequestMethod.POST)
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


	/**
	 * 会员头像
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/memberLogo","/rest/member/mc/base/sel_mem_logo"}, method = RequestMethod.GET)
	public Response memberLogo(HttpServletRequest req)  {
		Response result = new Response();
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		result.setData(member.getHeadShot());
		return result;
	}

	/**
	 * 资质保存
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/certificateSave","/rest/member/mc/company/add_certificate"}, method = RequestMethod.POST)
	public Response certificateSave(CertificateRecord record)  {
		Response result = new Response();
		record.setTime(new Date());
		memberService.saveCertificate(record);
		return result;
	}

	/**
	 * 资质编辑
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/certificateUpdate","/rest/member/mc/company/upd_certificate"}, method = RequestMethod.POST)
	public Response certificateUpdate(CertificateRecord record)  {
		Response result = new Response();
		record.setTime(new Date());
		memberService.updateCertificate(record);
		return result;

	}

	/**
	 * 资质删除
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/certificateDelete","/rest/member/mc/company/del_certificate"}, method = RequestMethod.POST)
	public Response certificateDelete(CertificateRecord record)  {
		Response result = new Response();
		memberService.deleteCertificate(record);
		return result;

	}

	/**
	 * 查询资质
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/certificateSearch","/rest/member/mc/company/sel_certificate"}, method = RequestMethod.GET)
	public Response certificateSearch(CertificateRecord record)  {
		Response result = new Response();
		List<CertificateRecord> list = memberService.certificateSearch(record);
		result.setData(list);
		return result;
	}

	/**
	 * 上传头像，返回url，并保存数据库
	 */
	@RequestMapping(value = {"/rest/uploadHeadShot","/rest/member/mc/base/upload_headShot"}, method = RequestMethod.POST)
	public Response uploadHeadShot(HttpServletRequest req, Member member) throws Exception {
		Response result = new Response();
		String url = uploadService.upload(req,"img");
		member.setHeadShot(url);
		memberService.uploadHeadShot(member);
		result.setData(url);
		return result;
	}

	/**
	 * 上传公司logo，返回url，并保存数据库
	 */
	@RequestMapping(value = {"/rest/uploadLogo","/rest/member/mc/company/upload_logo"}, method = RequestMethod.POST)
	public Response uploadLogo(HttpServletRequest req, Member member) throws Exception {
		Response result = new Response();
		String url = uploadService.upload(req,"img");
		member.setEnterpriseLogo(url);
		memberService.uploadLogo(member);
		result.setData(url);
		return result;
	}

	@RequestMapping(value = {"/rest/getNowTime","/rest/member/mc/base/get_now_time"}, method = RequestMethod.GET)
	public Response getNowTime() throws Exception {
		Response result = new Response();
		result.setCode(200);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		result.setData(df.format(date));
		return result;
	}
}
