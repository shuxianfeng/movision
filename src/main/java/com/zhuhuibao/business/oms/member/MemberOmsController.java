package com.zhuhuibao.business.oms.member;

import java.io.IOException;
import java.util.List;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Identity;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.OmsMemService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员管理
 * @author cxx
 *
 */
@RestController
@RequestMapping()
@Api(value = "MemberOmsController",description = "运营平台会员管理")
public class MemberOmsController {
	
	private static final Logger log = LoggerFactory.getLogger(MemberOmsController.class);

	@Autowired
	private OmsMemService omsMemService;

	@Autowired
	private MemberService memberService;

	/**
	 * 查询全部会员（分页）
	 * @param member
	 * @throws IOException
	 */
	@RequestMapping(value={"/rest/oms/getAllMemInfo","/rest/member/oms/base/sel_allMember"},method = RequestMethod.GET)
	public Response getAllMemInfo(OmsMemBean member, String pageNo, String pageSize) throws IOException
	{
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<OmsMemBean> pager = new Paging<OmsMemBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		Response result = omsMemService.getAllMemInfo(pager,member);
		return result;
	}

	/**
	 * 查询全部会员资质（分页）
	 * @param member
	 * @throws IOException
	 */
	@RequestMapping(value={"/rest/oms/getAllMemCertificate","/rest/member/oms/base/sel_allCertificate"},method = RequestMethod.GET)
	public Response getAllMemCertificate(OmsMemBean member, String pageNo, String pageSize) throws IOException
	{
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<OmsMemBean> pager = new Paging<OmsMemBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		Response result = omsMemService.getAllMemCertificate(pager,member);

		return result;
	}

	@RequestMapping(value="/rest/member/oms/base/sel_certificate",method = RequestMethod.GET)
	public Response queryCertificateById(String id) throws IOException
	{
		Response response = new Response();
		CertificateRecord certificateRecord = omsMemService.queryCertificateById(id);
		response.setData(certificateRecord);
		return response;
	}

	/**
	 * 会员状态审核
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = {"/rest/updateStatus","/rest/member/oms/base/upd_mem_status"}, method = RequestMethod.POST)
	@ApiOperation(value="会员状态审核",notes="会员状态审核",response = Response.class)
	public Response updateStatus(@ApiParam(value = "会员信息") @ModelAttribute(value="member")Member member)  {
		Response result = new Response();
		memberService.updateMemInfo(member);
		return result;
	}

	@RequestMapping(value = {"/rest/updateCertificate","/rest/member/oms/base/upd_certificate_status"}, method = RequestMethod.POST)
	@ApiOperation(value="资质审核",notes="资质审核",response = Response.class)
	public Response updateCertificate(@ApiParam(value = "资质信息") @ModelAttribute(value="record")CertificateRecord record)  {
		Response result = new Response();
		memberService.updateCertificate(record);
		return result;
	}

	@ApiOperation(value = "企业身份", notes = "企业身份", response = Response.class)
	@RequestMapping(value = "/rest/member/oms/base/sel_identityList", method = RequestMethod.GET)
	public Response identityList()  {
		Response result = new Response();
		List<Identity> identity = memberService.findIdentityList();
		result.setData(identity);
		return result;
	}

	@ApiOperation(value = "查询企业或用户详情", notes = "查询企业或用户详情", response = Response.class)
	@RequestMapping(value = "/rest/member/oms/base/sel_memberDetail", method = RequestMethod.GET)
	public Response sel_memberDetail(@ApiParam(value = "会员ID") @RequestParam String mem_id)  {
		Response result = new Response();
		Member member = memberService.findMemById(mem_id);
		result.setData(member);
		return result;
	}
}
