package com.zhuhuibao.business.oms.member;

import java.io.IOException;

import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.common.pojo.JsonResult;
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
public class MemManageController {
	
	private static final Logger log = LoggerFactory.getLogger(MemManageController.class);

	@Autowired
	private OmsMemService omsMemService;
	/**
	 * 查询全部会员（分页）
	 * @param member
	 * @throws IOException
	 */
	@RequestMapping(value="/rest/oms/getAllMemInfo",method = RequestMethod.GET)
	public JsonResult getAllMemInfo(OmsMemBean member, String pageNo, String pageSize) throws IOException
	{
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<OmsMemBean> pager = new Paging<OmsMemBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		JsonResult result = omsMemService.getAllMemInfo(pager,member);
		return result;
	}

	/**
	 * 查询全部会员资质（分页）
	 * @param member
	 * @throws IOException
	 */
	@RequestMapping(value="/rest/oms/getAllMemCertificate",method = RequestMethod.GET)
	public JsonResult getAllMemCertificate(OmsMemBean member, String pageNo, String pageSize) throws IOException
	{
		if (StringUtils.isEmpty(pageNo)) {
			pageNo = "1";
		}
		if (StringUtils.isEmpty(pageSize)) {
			pageSize = "10";
		}
		Paging<OmsMemBean> pager = new Paging<OmsMemBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
		JsonResult result = omsMemService.getAllMemCertificate(pager,member);

		return result;
	}
}
