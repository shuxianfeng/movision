package com.zhuhuibao.business.oms.member;

import java.io.IOException;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.OmsMemService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.RestController;

/**
 * 会员管理
 * @author cxx
 *
 */
@RestController
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
	public Response updateStatus(Member member)  {
		Response result = new Response();
		memberService.updateMemInfo(member);
		return result;
	}
}
