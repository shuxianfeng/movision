package com.zhuhuibao.business.memCenter.memManage.controller;

import com.zhuhuibao.mybatis.entity.Member;
import com.zhuhuibao.mybatis.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jianglz
 * @since 15/12/12.
 */
@RestController
public class InformationController {
	private static final Logger log = LoggerFactory
			.getLogger(InformationController.class);

	@Autowired
	private MemberService memberService;
	
	@Autowired
    private HttpServletRequest request;

	/**
	 * 个人基本信息完善
	 * @param req
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/memBasicInfo", method = RequestMethod.POST)
	public void register(HttpServletRequest req, HttpServletResponse response, Member member, Model model) throws IOException {
		memberService.updateMemBasicInfo(member);
	}


}
