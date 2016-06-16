package com.zhuhuibao.business.pay;

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
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
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

	@ApiOperation(value = "筑慧币充值", notes = "筑慧币充值", response = Response.class)
	@RequestMapping(value = "ach/add_achievement", method = RequestMethod.POST)
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
	@RequestMapping(value = "ach/add_achievement", method = RequestMethod.GET)
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
	@RequestMapping(value = "ach/add_achievement", method = RequestMethod.GET)
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
	@RequestMapping(value = "ach/add_achievement", method = RequestMethod.POST)
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
	
	
}
