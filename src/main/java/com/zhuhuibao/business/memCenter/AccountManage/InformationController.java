package com.zhuhuibao.business.memCenter.AccountManage;

import com.mysql.jdbc.StringUtils;
import com.zhuhuibao.common.ApiConstants;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
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

	@RequestMapping(value = "/rest/info", method = RequestMethod.GET)
	public JsonResult info(HttpServletRequest req, HttpServletResponse response) throws Exception {
		JsonResult result = new JsonResult();
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
	@RequestMapping(value = "/rest/detailInfo", method = RequestMethod.POST)
	public JsonResult detailInfo(Member member) throws Exception {
		JsonResult result = new JsonResult();
		memberService.updateMemInfo(member);
		return result;
	}

	/**
	 * 会员状态审核
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/updateStatus", method = RequestMethod.POST)
	public JsonResult updateStatus(Member member) throws Exception {
		JsonResult result = new JsonResult();
		memberService.updateStatus(member);
		return result;
	}

	/**
	 * 资质类型
	 * @param req
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/certificateList", method = RequestMethod.GET)
	public JsonResult certificateList(HttpServletRequest req) throws Exception {
		JsonResult jsonResult = new JsonResult();
		String type = req.getParameter("type");
		List list = memberService.findCertificateList(type);
		jsonResult.setData(list);
		return jsonResult;
	}

	/**
	 * 工作类别
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/workTypeList", method = RequestMethod.GET)
	public JsonResult workTypeList() throws Exception {
		JsonResult result = new JsonResult();
		List<WorkType> workType = memberService.findWorkTypeList();
		result.setData(workType);
		return result;
	}

	/**
	 * 企业性质
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/enterpriseTypeList", method = RequestMethod.GET)
	public JsonResult enterpriseTypeList() throws Exception {
		JsonResult result = new JsonResult();
		List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
		result.setData(enterpriseType);
		return result;
	}

	/**
	 * 企业身份
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/identityList", method = RequestMethod.GET)
	public JsonResult identityList() throws Exception {
		JsonResult result = new JsonResult();
		List<Identity> identity = memberService.findIdentityList();
		result.setData(identity);
		return result;
	}


	/**
	 * 人员规模
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/employeeSizeList", method = RequestMethod.GET)
	public JsonResult employeeSizeList() throws Exception {
		JsonResult result = new JsonResult();
		List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
		result.setData(employeeSizeList);
		return result;
	}

	/**
	 * 查询省
	 * @return
	 * @throws IOException
	 */

	@RequestMapping(value = "/rest/searchProvince", method = RequestMethod.GET)
	public JsonResult searchProvince() throws Exception {
		JsonResult result = new JsonResult();
		List<ResultBean> province = memberService.findProvince();
		result.setData(province);
		return result;
	}

	/**
	 * 根据省Code查询市
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/searchCity", method = RequestMethod.GET)
	public JsonResult searchCity(String provincecode) throws Exception {
		JsonResult result = new JsonResult();
		List<ResultBean> city = memberService.findCity(provincecode);
		result.setData(city);
		return result;
	}

	/**
	 * 根据市Code查询县区
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/searchArea", method = RequestMethod.GET)
	public JsonResult searchArea(String cityCode) throws Exception {
		JsonResult result = new JsonResult();
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
	@RequestMapping(value = "/rest/isBindMobile", method = RequestMethod.POST)
	public JsonResult isBindMobile(HttpServletRequest req) throws Exception {
		String memId = req.getParameter("id");
		JsonResult result = new JsonResult();
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
	@RequestMapping(value = "/rest/isBindEmail", method = RequestMethod.POST)
	public JsonResult isBindEmail(HttpServletRequest req) throws Exception {
		String memId = req.getParameter("id");
		JsonResult result = new JsonResult();
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
	@RequestMapping(value = "/rest/memberLogo", method = RequestMethod.GET)
	public JsonResult memberLogo(HttpServletRequest req) throws Exception {
		JsonResult result = new JsonResult();
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
	@RequestMapping(value = "/rest/certificateSave", method = RequestMethod.POST)
	public JsonResult certificateSave(CertificateRecord record) throws Exception {
		JsonResult result = new JsonResult();
		record.setTime(new Date());
		memberService.saveCertificate(record);
		return result;
	}

	/**
	 * 资质编辑
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateUpdate", method = RequestMethod.POST)
	public JsonResult certificateUpdate(CertificateRecord record) throws Exception {
		JsonResult result = new JsonResult();
		record.setTime(new Date());
		memberService.updateCertificate(record);
		return result;

	}

	/**
	 * 资质删除
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateDelete", method = RequestMethod.POST)
	public JsonResult certificateDelete(CertificateRecord record) throws Exception {
		JsonResult result = new JsonResult();
		memberService.deleteCertificate(record);
		return result;

	}

	/**
	 * 查询资质
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/certificateSearch", method = RequestMethod.GET)
	public JsonResult certificateSearch(CertificateRecord record) throws Exception {
		JsonResult result = new JsonResult();
		List<CertificateRecord> list = memberService.certificateSearch(record);
		result.setData(list);
		return result;
	}

	/**
	 * 上传头像，返回url，并保存数据库
	 */
	@RequestMapping(value = "/rest/uploadHeadShot", method = RequestMethod.POST)
	public JsonResult uploadHeadShot(HttpServletRequest req, Member member) throws Exception {
		JsonResult result = new JsonResult();
		String url = uploadService.upload(req,"img");
		member.setHeadShot(url);
		memberService.uploadHeadShot(member);
		result.setData(url);
		return result;
	}

	/**
	 * 上传公司logo，返回url，并保存数据库
	 */
	@RequestMapping(value = "/rest/uploadLogo", method = RequestMethod.POST)
	public JsonResult uploadLogo(HttpServletRequest req, Member member) throws Exception {
		JsonResult result = new JsonResult();
		String url = uploadService.upload(req,"img");
		member.setEnterpriseLogo(url);
		memberService.uploadLogo(member);
		result.setData(url);
		return result;
	}

	@RequestMapping(value = "/rest/getNowTime", method = RequestMethod.GET)
	public JsonResult getNowTime() throws Exception {
		JsonResult result = new JsonResult();
		result.setCode(200);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		result.setData(df.format(date));
		return result;
	}
}
