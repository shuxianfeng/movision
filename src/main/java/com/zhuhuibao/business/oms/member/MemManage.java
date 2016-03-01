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

/**
 * 会员管理
 * @author penglong
 *
 */
public class MemManage {
	
	private static final Logger log = LoggerFactory.getLogger(MemManage.class);
	
	/**
	 * 
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @throws IOException
	 */
	@RequestMapping(value="/osm/get",method = RequestMethod.POST)
	public void register(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException
	{
		
	}
}
