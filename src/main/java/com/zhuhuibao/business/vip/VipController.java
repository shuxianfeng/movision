package com.zhuhuibao.business.vip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.business.zhb.ZhbController;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.VipConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.vip.entity.VipMemberInfo;
import com.zhuhuibao.mybatis.vip.entity.VipPrivilege;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * vip
 * 
 * @author tongxinglong
 *
 */
@RestController
@RequestMapping("/rest/vip")
public class VipController {
	
	private static final Logger log = LoggerFactory.getLogger(VipController.class);
	
	@Autowired
	private ZhbService zhbService;

	@Autowired
	private VipInfoService vipInfoService;
	
	
	
	@ApiOperation(value = "添加尊贵盟友", notes = "添加尊贵盟友 ", response = Response.class)
	@RequestMapping(value = "mc/add_vip", method = RequestMethod.POST)
	public Response addVipService(
			@ApiParam(value = "合同编号") @RequestParam(required = true) String contractNo,
			@ApiParam(value = "尊贵盟友账号") @RequestParam(required = true) String member_account,
			@ApiParam(value = "套餐类型") @RequestParam(required = true) int vip_level,
			@ApiParam(value = "生效时间") @RequestParam(required = true) String active_time,
			@ApiParam(value = "套餐有效期") @RequestParam(required = true) int validity
			)
			throws Exception {
		Response response = new Response();
		// 开通VIP服务
		int result = 0;
		try {
			result = vipInfoService.addVipService(contractNo, member_account, vip_level, active_time, validity);
		} catch (Exception e) {
			log.error("执行异常>>>",e);
			response.setMessage(e.getMessage());
		}
		response.setData(result);
		response.setCode(1 == result ? 200 : 400);

		return response;
	}

	@ApiOperation(value = "VIP介绍页面", notes = "VIP介绍页面", response = Response.class)
	@RequestMapping(value = "site/sel_vippack", method = RequestMethod.GET)
	public Response vippack() throws Exception {
		Response response = new Response();
		Map<String, Object> result = new HashMap<String, Object>();
		List<DictionaryZhbgoods> goodsList = zhbService.listZhbGoodsByType("2");
		result.put("goodsList", goodsList);
		result.put("account", ShiroUtil.getMember().getAccount());
		response.setData(result);
		return response;
	}

	@ApiOperation(value = "VIP购买确认页", notes = "VIP购买确认页", response = Response.class)
	@RequestMapping(value = "site/sel_buyvippack", method = RequestMethod.GET)
	public Response buyVipPack(@ApiParam(value = "个人版VIP标志(1:个人，0：企业)") @RequestParam(required = false) String personFlag) throws Exception {
		Response response = new Response();
		Map<String, Object> result = new HashMap<String, Object>();
		List<DictionaryZhbgoods> allVipLevel = zhbService.listZhbGoodsByType("2");
		List<DictionaryZhbgoods> vipLevels = new ArrayList<DictionaryZhbgoods>();
		boolean isPerson = "1".equals(personFlag);
		if (CollectionUtils.isNotEmpty(allVipLevel)) {
			for (DictionaryZhbgoods goods : allVipLevel) {
				if (isPerson && Integer.valueOf(goods.getValue()) < 100) {
					vipLevels.add(goods);
				} else if (!isPerson && Integer.valueOf(goods.getValue()) > 100) {
					vipLevels.add(goods);
				}
			}
		}
		result.put("vipLevels", vipLevels);
		result.put("account", ShiroUtil.getMember().getAccount());
		response.setData(result);
		return response;
	}

	@ApiOperation(value = "我的VIP特权", notes = "我的VIP特权", response = Response.class)
	@RequestMapping(value = "mc/sel_myvip", method = RequestMethod.GET)
	public Response myvip() throws Exception {
		Response response = new Response();
		Map<String, Object> result = new HashMap<String, Object>();
		VipMemberInfo vipMemberInfo = vipInfoService.findVipMemberInfoById(ShiroUtil.getMember().getCompanyId());
		List<VipPrivilege> privilegeList = vipInfoService.listVipPrivilegeByLevel(vipMemberInfo.getVipLevel());
		String vipName = VipConstant.VIP_LEVEL_NAME.get(String.valueOf(vipMemberInfo.getVipLevel()));
		result.put("vipMemberInfo", vipMemberInfo);
		result.put("vipName", vipName);
		result.put("privilegeList", privilegeList);

		response.setData(result);
		return response;
	}

	@ApiOperation(value = "运营管理系统-VIP会员", notes = "运营管理系统-VIP会员", response = Response.class)
	@RequestMapping(value = "oms/sel_allvip", method = RequestMethod.GET)
	public Response omsAllvip(@ApiParam(value = "登陆账号") @RequestParam(required = false) String account,
			@ApiParam(value = "姓名/企业名称") @RequestParam(required = false) String name,
			@ApiParam(value = "Vip会员等级") @RequestParam(required = false) String vipLevel,
			@ApiParam(value = "Vip会员状态") @RequestParam(required = false) String status,
			@ApiParam(value = "页码") @RequestParam(required = false,defaultValue = "1") String pageNo,
			@ApiParam(value = "每页显示的条数") @RequestParam(required = false,defaultValue = "10") String pageSize) throws Exception {
		Response response = new Response();

		List<Map<String, String>> vipList;

		Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
		vipList = vipInfoService.listAllVipInfo(account, name, vipLevel, status, pager);
		pager.result(vipList);
		response.setData(pager);
		return response;
	}
}
