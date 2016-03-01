package com.zhuhuibao.business.memCenter.memManage.controller;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Certificate;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.CertificateMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.JsonUtils;
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
import java.util.List;

/**
 * @author cxx
 * @since 16/2/25.
 */
@RestController
public class InformationController {
	private static final Logger log = LoggerFactory
			.getLogger(InformationController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CertificateMapper certificateMapper;

	/**
	 * 个人基本信息页面
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/info", method = RequestMethod.GET)
	public void companyInfo(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(member));
	}
	/*@RequestMapping(value = "/rest/info", method = RequestMethod.GET)
	public void info(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		Map<String, String> map=new HashMap<String,String>();
		map.put("personRealName",member.getPersonRealName());
		if(member.getPersonSex()!=null){
			map.put("personSex",member.getPersonSex().toString());
		}else{
			map.put("personSex",null);
		}
		if(member.getPersonPosition()!=null){
			map.put("personPosition",member.getPersonPosition().toString());
		}else{
			map.put("personPosition",null);
		}

		map.put("personProvince",member.getPersonProvince());
		map.put("personCity",member.getPersonCity());
		map.put("personArea",member.getPersonArea());
		map.put("personAddress",member.getPersonAddress());
		if(member.getPersonMobile()!=null){
			map.put("personMobile",member.getPersonMobile().toString());
		}else{
			map.put("personMobile",null);
		}
		if(member.getPersonTel()!=null){
			map.put("personTel",member.getPersonTel().toString());
		}else{
			map.put("personTel",null);
		}
		if(member.getPersonQQ()!=null){
			map.put("personQQ",member.getPersonQQ().toString());
		}else{
			map.put("personQQ",null);
		}
		map.put("personHeadShot",member.getPersonHeadShot());
		if(member.getPersonID()!=null){
			map.put("personID",member.getPersonID().toString());
		}else{
			map.put("personID",null);
		}

		map.put("personIDFrontImgUrl",member.getPersonIDFrontImgUrl());
		map.put("personIDBackImgUrl",member.getPersonIDBackImgUrl());
		map.put("personCertificateName",member.getPersonCertificateName());
		map.put("personCertificateOrg",member.getPersonCertificateOrg());
		map.put("personCertificateUrl",member.getPersonCertificateUrl());
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(map));
	}*/

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
		if(member.getPersonRealName()==null){
			result.setCode(400);
			result.setMessage("真实姓名为必填项");
		}else if(member.getPersonSex()==null){
			result.setCode(400);
			result.setMessage("性别为必填项");
		}else if(member.getPersonPosition()==null){
			result.setCode(400);
			result.setMessage("工作类型为必填项");
		}else if(member.getPersonProvince() == null || member.getPersonCity() == null || member.getPersonArea() == null){
			result.setCode(400);
			result.setMessage("所在地为必填项");
		}else{
			int isUpdate = memberService.updateMemInfo(member);
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

	/**
	 * 企业基本信息完善
	 * @param req
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/companyBasicInfo", method = RequestMethod.POST)
	public void companyBasicInfo(HttpServletRequest req, HttpServletResponse response, Member member, Model model) throws IOException {
		JsonResult result = new JsonResult();
		if(member.getCompanyName()==null){
			result.setCode(400);
			result.setMessage("企业名称为必填项");
		}else if(member.getEnterpriseMemberType()==null){
			result.setCode(400);
			result.setMessage("经营方式为必填项");
		}else if(member.getProvince()==null||member.getCity()==null||member.getArea()==null){
			result.setCode(400);
			result.setMessage("所在地为必填项");
		}else{
			int isUpdate = memberService.updateMemInfo(member);
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


	/**
	 * 详细信息，联系方式完善（个人，企业）
	 * @param req
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/detailInfo", method = RequestMethod.POST)
	public void detailInfo(HttpServletRequest req, HttpServletResponse response, Member member, Model model) throws IOException {
		JsonResult result = new JsonResult();

		int isUpdate = memberService.updateMemInfo(member);
		if (isUpdate == 0) {
			result.setCode(400);
			result.setMessage("保存失败");
		} else {
			result.setCode(200);
		}

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 资质类型
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/certificateList", method = RequestMethod.GET)
	public void certificateList(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<Certificate> certificate = certificateMapper.findCertificateList();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(certificate));
	}

	//上传图片
}
