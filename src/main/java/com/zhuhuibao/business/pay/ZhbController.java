package com.zhuhuibao.business.pay;

import java.math.BigDecimal;
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
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;

/**
 * 筑慧币
 * 
 * @author tongxinglong
 *
 */
@RestController
@RequestMapping("/rest/zhb")
public class ZhbController {

	@Autowired
	private ZhbService zhbService;

	@Autowired
	private VipInfoService vipInfoService;

	@ApiOperation(value = "筑慧币充值", notes = "筑慧币充值，返回0：失败，1：成功", response = Response.class)
	@RequestMapping(value = "mc/upd_prepaid", method = RequestMethod.POST)
	public Response zhbPrepaid(@ApiParam(value = "订单编号") @RequestParam(required = true) String orderNo) throws Exception {
		Response response = new Response();
		// 判断登录者与操作数据是否属于同一人
		int result = 0;
		try {
			result = zhbService.zhbPrepaidByOrder(orderNo);
		} catch (Exception e) {
		}
		response.setData(result);

		return response;
	}

	@ApiOperation(value = "VIP服务开通", notes = "VIP服务开通，返回1：成功，0：失败", response = Response.class)
	@RequestMapping(value = "mc/upd_openvip", method = RequestMethod.POST)
	public Response openVipService(@ApiParam(value = "订单编号") @RequestParam(required = true) String orderNo) throws Exception {
		Response response = new Response();
		// 开通VIP服务
		int result = 0;
		try {
			result = zhbService.openVipService(orderNo);
		} catch (Exception e) {
		}
		response.setData(result);

		return response;
	}

	@ApiOperation(value = "筑慧币余额查询", notes = "筑慧币余额查询", response = Response.class)
	@RequestMapping(value = "mc/sel_amount", method = RequestMethod.GET)
	public Response getZhbAccount() throws Exception {
		Response response = new Response();
		ZhbAccount zhbAccount = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
		response.setData(zhbAccount);

		return response;
	}

	@ApiOperation(value = "筑慧币订单支付", notes = "筑慧币订单支付，返回0：失败，1：成功", response = Response.class)
	@RequestMapping(value = "mc/upd_payfororder", method = RequestMethod.POST)
	public Response payForOrder(@ApiParam(value = "订单号") @RequestParam String orderNo, @ApiParam(value = "支付金额") @RequestParam BigDecimal zhbAmount)
			throws Exception {
		Response response = new Response();

		int result = 0;
		try {
			result = zhbService.payForOrder(orderNo, zhbAmount);
		} catch (Exception e) {
		}
		response.setData(result);

		return response;
	}

	@ApiOperation(value = "筑慧币业务消费支付", notes = "筑慧币业务消费支付", response = Response.class)
	@RequestMapping(value = "mc/upd_payforgoods", method = RequestMethod.POST)
	public Response payForGoods(@ApiParam(value = "物品ID") @RequestParam Long goodsId, @ApiParam(value = "物品类型") @RequestParam String goodsType) throws Exception {
		Response response = new Response();

		int result = 0;
		try {
			result = zhbService.payForGoods(goodsId, goodsType);
		} catch (Exception e) {
		}

		response.setData(result);

		return response;
	}

	@ApiOperation(value = "筑慧币退款", notes = "筑慧币退款，返回0：失败，1：成功", response = Response.class)
	@RequestMapping(value = "mc/upd_refund", method = RequestMethod.POST)
	public Response refund(@ApiParam(value = "订单号") @RequestParam String orderNo) throws Exception {
		Response response = new Response();

		int result = 0;
		try {
			result = zhbService.refundBySystem(orderNo);
		} catch (Exception e) {
		}

		response.setData(result);

		return response;
	}

	@ApiOperation(value = "筑慧币余额及当前业务价格查询", notes = "筑慧币余额及当前业务价格查询", response = Response.class)
	@RequestMapping(value = "/sel_price", method = RequestMethod.GET)
	public Response selZhbPriceAndAmount(@ApiParam(value = "物品类型") @RequestParam(required = true) String goodsType) throws Exception {
		Response response = new Response();
		Map<String, String> zhbInfo = new HashMap<String, String>();
		// 剩余特权数量
		long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), goodsType);
		zhbInfo.put("privilegeNum", String.valueOf(privilegeNum));
		if (privilegeNum <= 0) {
			// 筑慧币单价
			DictionaryZhbgoods goodsConfig = zhbService.getZhbGoodsByPinyin(goodsType);
			zhbInfo.put("zhbPrice", null != goodsConfig ? String.valueOf(goodsConfig.getPriceDoubleValue()) : "999");
			// 筑慧币余额
			ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
			zhbInfo.put("zhbAmount", null != account ? account.getAmount().toString() : "0");
		}

		response.setData(zhbInfo);
		return response;
	}

	@ApiOperation(value = "筑慧币明细记录", notes = "筑慧币明细记录", response = Response.class)
	@RequestMapping(value = "mc/record/sel_recordList", method = RequestMethod.POST)
	public Response getRecordList(@ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
			@ApiParam(value = "每页显示的条数") @RequestParam(required = false) String pageSize,
			@ApiParam(value = "数据类型，1:使用,2:获取,null:所有") @RequestParam(required = false) String recordType) throws Exception {
		Response response = new Response();
		List<Map<String, String>> zhbDetails = zhbService.getZhbDetails(pageNo, pageSize, recordType);
		ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("zhbDetails", zhbDetails);
		result.put("account", account);
		response.setData(result);

		return response;
	}

}
