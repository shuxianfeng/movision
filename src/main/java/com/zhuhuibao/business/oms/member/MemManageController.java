package com.zhuhuibao.business.oms.member;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhuhuibao.mybatis.memberReg.entity.Member;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员管理
 * @author cxx
 *
 */
@RestController
public class MemManageController {
	
	private static final Logger log = LoggerFactory.getLogger(MemManageController.class);
	
	/**
	 * 
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value="/oms/getAllMemInfo",method = RequestMethod.GET)
	public void register(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException
	{
		
	}
}
