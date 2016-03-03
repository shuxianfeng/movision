package com.zhuhuibao.business.memCenter.memManage.controller;

import com.mysql.jdbc.StringUtils;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.mapper.*;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private UnitTypeMapper unitTypeMapper;

	@Autowired
	private WorkTypeMapper workTypeMapper;

	@Autowired
	private EnterpriseTypeMapper enterpriseTypeMapper;

	@Autowired
	private CompanyIdentityMapper companyIdentityMapper;

	@Autowired
	private ProvinceMapper provinceMapper;

	@Autowired
	private CityMapper cityMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Autowired
	private CertificateRecordMapper certificateRecordMapper;

	/**
	 * 根据ID查询会员所有信息
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/info", method = RequestMethod.GET)
	public void info(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(member));
	}

	/**
	 * 个人基本信息保存
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
		}else if(member.getSex()==null){
			result.setCode(400);
			result.setMessage("性别为必填项");
		}else if(member.getPersonPosition()==null){
			result.setCode(400);
			result.setMessage("工作类型为必填项");
		}else if(member.getProvince() == null || member.getCity() == null || member.getArea() == null){
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
	 * 企业基本信息保存
	 * @param req
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/companyBasicInfo", method = RequestMethod.POST)
	public void companyBasicInfo(HttpServletRequest req, HttpServletResponse response, Member member, Model model) throws IOException {
		JsonResult result = new JsonResult();
		if(member.getEnterpriseName()==null){
			result.setCode(400);
			result.setMessage("企业名称为必填项");
		}else if(member.getCompanyIdentify()==null){
			result.setCode(400);
			result.setMessage("企业身份为必填项");
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
	 * 详细信息，联系方式保存（个人，企业）
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
		String type = req.getParameter("type");
		List<Certificate> certificate = certificateMapper.findCertificateList(type);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(certificate));
	}

	/**
	 * 工作单位类别
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/unitTypeList", method = RequestMethod.GET)
	public void unitTypeList(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<UnitType> unitType = unitTypeMapper.findUnitTypeList();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(unitType));
	}

	/**
	 * 工作类别
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/workTypeList", method = RequestMethod.GET)
	public void workTypeList(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<WorkType> workType = workTypeMapper.findWorkTypeList();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(workType));
	}

	/**
	 * 企业类别
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/enterpriseTypeList", method = RequestMethod.GET)
	public void enterpriseTypeList(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<EnterpriseType> enterpriseType = enterpriseTypeMapper.findEnterpriseTypeList();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(enterpriseType));
	}

	/**
	 * 企业身份
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/companyIdentityList", method = RequestMethod.GET)
	public void companyIdentityList(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<CompanyIdentity> companyIdentity = companyIdentityMapper.findCompanyIdentityList();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(companyIdentity));
	}

	/**
	 * 查询省
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/searchProvince", method = RequestMethod.GET)
	public void searchProvince(HttpServletRequest req, HttpServletResponse response) throws IOException {
		List<ResultBean> province = provinceMapper.findProvince();
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(province));
	}

	/**
	 * 根据省Code查询市
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/searchCity", method = RequestMethod.GET)
	public void searchCity(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String provincecode = req.getParameter("provincecode");
		List<ResultBean> city = cityMapper.findCity(provincecode);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(city));
	}

	/**
	 * 根据市Code查询县区
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/searchArea", method = RequestMethod.GET)
	public void searchArea(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String cityCode = req.getParameter("cityCode");
		List<ResultBean> area = areaMapper.findArea(cityCode);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(area));
	}

	/**
	 * 判断是否绑定手机
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/isBindMobile", method = RequestMethod.GET)
	public void isBindMobile(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		JsonResult result = new JsonResult();
		if(member.getMobile()!=null || !StringUtils.isNullOrEmpty(member.getMobile())){
			result.setCode(200);
			result.setMessage("手机已绑定");
		}else{
			result.setCode(400);
			result.setMessage("手机未绑定");
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 判断是否绑定邮箱
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/isBindEmail", method = RequestMethod.GET)
	public void isBindEmail(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		JsonResult result = new JsonResult();
		if(member.getEmail()!=null || !StringUtils.isNullOrEmpty(member.getEmail())){
			result.setCode(200);
			result.setMessage("邮箱已绑定");
		}else{
			result.setCode(400);
			result.setMessage("邮箱未绑定");
		}
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}

	/**
	 * 会员头像
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/memberLogo", method = RequestMethod.GET)
	public void memberLogo(HttpServletRequest req, HttpServletResponse response) throws IOException {
		String memId = req.getParameter("id");
		Member member = memberService.findMemById(memId);
		response.getWriter().write(member.getHeadShot());
	}

	/**
	 * 资质保存
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateSave", method = RequestMethod.POST)
	public void certificateSave(HttpServletRequest req, HttpServletResponse response,CertificateRecord record) throws IOException {
		JsonResult result = new JsonResult();
		String certificate_grade = record.getCertificate_grade().toString();
		if("1".equals(certificate_grade)){
			record.setCertificate_grade_name("一级");
		}else if("2".equals(certificate_grade)){
			record.setCertificate_grade_name("二级");
		}else if("3".equals(certificate_grade)){
			record.setCertificate_grade_name("三级");
		}else if("4".equals(certificate_grade)){
			record.setCertificate_grade_name("甲级");
		}else{
			record.setCertificate_grade_name("乙级");
		}
		record.setTime(new Date());
		int isSave = certificateRecordMapper.saveCertificate(record);
		if(isSave==1){
			result.setCode(200);
		}else{
			result.setCode(400);
			result.setMessage("资质保存失败");
		}

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));

	}

	/**
	 * 资质编辑
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateUpdate", method = RequestMethod.POST)
	public void certificateUpdate(HttpServletRequest req, HttpServletResponse response,CertificateRecord record) throws IOException {
		JsonResult result = new JsonResult();
		String certificate_grade = record.getCertificate_grade().toString();
		if("1".equals(certificate_grade)){
			record.setCertificate_grade_name("一级");
		}else if("2".equals(certificate_grade)){
			record.setCertificate_grade_name("二级");
		}else if("3".equals(certificate_grade)){
			record.setCertificate_grade_name("三级");
		}else if("4".equals(certificate_grade)){
			record.setCertificate_grade_name("甲级");
		}else{
			record.setCertificate_grade_name("乙级");
		}
		record.setTime(new Date());
		int isUpdate = certificateRecordMapper.updateCertificate(record);
		if(isUpdate==1){
			result.setCode(200);
		}else{
			result.setCode(400);
			result.setMessage("资质编辑失败");
		}

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));

	}

	/**
	 * 资质删除
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateDelete", method = RequestMethod.POST)
	public void certificateDelete(HttpServletRequest req, HttpServletResponse response,CertificateRecord record) throws IOException {
		JsonResult result = new JsonResult();
		int isDelete = certificateRecordMapper.deleteCertificate(record);
		if(isDelete==1){
			result.setCode(200);
		}else{
			result.setCode(400);
			result.setMessage("资质删除失败");
		}

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));

	}

	/**
	 * 查询资质
	 * @param req
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateSearch", method = RequestMethod.GET)
	public void certificateSearch(HttpServletRequest req, HttpServletResponse response,CertificateRecord record) throws IOException {
		List<CertificateRecord> list = certificateRecordMapper.certificateSearch(record);

		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(list));

	}
}
