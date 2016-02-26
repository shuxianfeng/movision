package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;

/**
 * 会员中心业务处理
 * @author cuixiaoxiao
 *
 */
@Service
@Transactional
public class MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberMapper memberMapper;

	/**
	 * 个人基本信息完善
	 */
	public int updateMemBasicInfo(Member member)
	{
		log.debug("个人基本信息完善");
		int result = 0;
		result = memberMapper.updateMemBasicInfo(member);
		return result;
	}



}
