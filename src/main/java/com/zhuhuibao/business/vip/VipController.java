package com.zhuhuibao.business.vip;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;

/**
 * vip
 * 
 * @author tongxinglong
 *
 */
@RestController
@RequestMapping("/rest/vip")
public class VipController {

	@Autowired
	private ZhbService zhbService;

	@Autowired
	private VipInfoService vipInfoService;

	@ApiOperation(value = "VIP介绍页面", notes = "进入筑慧币充值页", response = Response.class)
	@RequestMapping(value = "site/sel_vippack", method = RequestMethod.GET)
	public Response vippack(@ApiParam(value = "订单编号") @RequestParam(required = true) String orderNo) throws Exception {
		Response response = new Response();
		Map<String, Object> result = new HashMap<String, Object>();
		List<DictionaryZhbgoods> goodsList = zhbService.listZhbGoodsByType("2");
		result.put("goodsList", goodsList);
		result.put("account", ShiroUtil.getMember().getAccount());
		response.setData(result);
		return response;
	}

}
