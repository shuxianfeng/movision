package com.zhuhuibao.business.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbRecord;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;

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

	@ApiOperation(value = "筑慧币充值", notes = "筑慧币充值", response = Response.class)
	@RequestMapping(value = "mc/upd_prepaid", method = RequestMethod.POST)
	public Response zhbPrepaid(@ApiParam(value = "服务类型") @RequestParam(required = true) String orderNo) throws Exception {
		Response response = new Response();
		// 判断登录者与操作数据是否属于同一人
		int result = zhbService.zhbPrepaidByOrder(orderNo);
		if (1 == result) {
			// 充值成功
			response.setCode(200);

		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "VIP服务开通", notes = "VIP服务开通", response = Response.class)
	@RequestMapping(value = "mc/upd_openvip", method = RequestMethod.POST)
	public Response openVipService(@ApiParam(value = "服务类型") @RequestParam(required = true) String orderNo) throws Exception {
		Response response = new Response();
		// 开通VIP服务
		int result = zhbService.openVipService(orderNo);
		if (1 == result) {
			// 充值成功
			response.setCode(200);

		} else {
			response.setCode(400);
		}

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

	@ApiOperation(value = "筑慧币支付", notes = "筑慧币支付", response = Response.class)
	@RequestMapping(value = "mc/upd_payfor", method = RequestMethod.POST)
	public Response payForOrder(@ApiParam(value = "订单号") @RequestParam String orderNo, @ApiParam(value = "支付金额") @RequestParam BigDecimal zhbAmount)
			throws Exception {
		Response response = new Response();

		int result = zhbService.payForOrder(orderNo, zhbAmount);
		if (1 == result) {
			response.setCode(200);
		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "筑慧币退款", notes = "筑慧币退款", response = Response.class)
	@RequestMapping(value = "mc/upd_refund", method = RequestMethod.POST)
	public Response refund(@ApiParam(value = "订单号") @RequestParam String orderNo) throws Exception {
		Response response = new Response();

		int result = zhbService.refundBySystem(orderNo);
		if (1 == result) {
			response.setCode(200);
		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "筑慧币余额及当前业务价格查询", notes = "筑慧币余额及当前业务价格查询", response = Response.class)
	@RequestMapping(value = "/sel_price", method = RequestMethod.GET)
	public Response selZhbPriceAndAmount(@ApiParam(value = "服务类型") @RequestParam(required = true) String goodsType) throws Exception {
		Response response = new Response();
		Map<String, String> zhbInfo = new HashMap<String, String>();
		// 剩余特权数量
		long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), goodsType);
		if (privilegeNum > 0) {
			zhbInfo.put("privilegeNum", String.valueOf(privilegeNum));
		} else {
			// 筑慧币单价
			DictionaryZhbgoods goodsConfig = zhbService.getZhbGoodsByPinyin(goodsType);
			zhbInfo.put("zhbPrice", goodsConfig.getPinyin().toString());
			// 筑慧币余额
			ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
			zhbInfo.put("zhbAmount", account.getAmount().toString());
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

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("zhbDetails", zhbDetails);
		data.put("account", account);

		return response;
	}

}
