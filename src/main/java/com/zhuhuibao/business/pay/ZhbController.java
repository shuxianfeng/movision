package com.zhuhuibao.business.pay;

import java.util.HashMap;
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
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.entity.ZhbGoodsConfig;
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
	public Response zhbPrepaid(@ModelAttribute ZhbRecord zhbRecord) throws Exception {
		Response response = new Response();

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		ShiroUser member = (ShiroUser) session.getAttribute("member");
		// 判断登录者与操作数据是否属于同一人
		if (null != member && member.getId() == zhbRecord.getOperaterId()) {
			int result = zhbService.zhbPrepaid(zhbRecord);
			if (1 == result) {
				// 充值成功
				response.setCode(200);

			} else {
				response.setCode(400);
			}
		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "筑慧币余额查询", notes = "筑慧币余额查询", response = Response.class)
	@RequestMapping(value = "mc/sel_amount", method = RequestMethod.GET)
	public Response getZhbAccount(@ApiParam(value = "会员ID") @RequestParam Long memberId) throws Exception {
		Response response = new Response();

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		ShiroUser member = (ShiroUser) session.getAttribute("member");
		if (null != member && member.getId() == memberId) {
			ZhbAccount zhbAccount = zhbService.getZhbAccount(memberId);
			response.setData(zhbAccount);
		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "筑慧币支付", notes = "筑慧币支付", response = Response.class)
	@RequestMapping(value = "mc/upd_payfor", method = RequestMethod.POST)
	public Response payForOrder(@ModelAttribute ZhbRecord zhbRecord) throws Exception {
		Response response = new Response();

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		ShiroUser member = (ShiroUser) session.getAttribute("member");
		if (null != member && member.getId() == zhbRecord.getOperaterId()) {
			int result = zhbService.payForOrder(zhbRecord);
			if (1 == result) {
				response.setCode(200);
			} else {
				response.setCode(400);
			}

		} else {
			response.setCode(400);
		}

		return response;
	}

	@ApiOperation(value = "筑慧币退款", notes = "筑慧币退款", response = Response.class)
	@RequestMapping(value = "mc/upd_refund", method = RequestMethod.POST)
	public Response refund(@ModelAttribute ZhbRecord zhbRecord) throws Exception {
		Response response = new Response();

		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		ShiroUser member = (ShiroUser) session.getAttribute("member");
		if (null != member && member.getId() == zhbRecord.getOperaterId()) {
			int result = zhbService.refund(zhbRecord);
			if (1 == result) {
				response.setCode(200);
			} else {
				response.setCode(400);
			}

		} else {
			response.setCode(400);
		}
		return response;
	}

	@ApiOperation(value = "筑慧币余额及当前业务价格查询", notes = "筑慧币余额及当前业务价格查询", response = Response.class)
	@RequestMapping(value = "/sel_price", method = RequestMethod.GET)
	public Response selZhbPriceAndAmount(@ApiParam(value = "服务类型") @RequestParam(required = true) String goodsType) throws Exception {
		Response response = new Response();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession(false);
		ShiroUser member = (ShiroUser) session.getAttribute("member");
		Map<String, String> zhbInfo = new HashMap<String, String>();
		if (null != member) {
			// 剩余特权数量
			long privilegeNum = vipInfoService.getExtraPrivilegeNum(member.getCompanyId(), goodsType);
			if (privilegeNum > 0) {
				zhbInfo.put("privilegeNum", String.valueOf(privilegeNum));
			} else {
				// 筑慧币单价
				ZhbGoodsConfig goodsConfig = zhbService.getZhbGoodsConfigByPinyin(goodsType);
				zhbInfo.put("zhbPrice", goodsConfig.getPinyin().toString());
				// 筑慧币余额
				ZhbAccount account = zhbService.getZhbAccount(member.getCompanyId());
				zhbInfo.put("zhbAmount", account.getAmount().toString());
			}

		} else {
			response.setCode(400);
		}
		
		response.setData(zhbInfo);
		return response;
	}
}
