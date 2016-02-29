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
 * @author cxx
 *
 */
@Service
@Transactional
public class MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);

	@Autowired
	private MemberMapper memberMapper;

	/**
	 * 会员信息完善
	 */
	public int updateMemInfo(Member member)
	{
		log.debug("会员信息完善");
		int result = 0;
		result = memberMapper.updateMemInfo(member);
		return result;
	}

	/**
	 * 根据会员ID查询会员信息
	 */
	public Member findMemById(String id)
	{
		log.debug("根据会员ID查询会员信息");
		Member member = memberMapper.findMemById(id);
		return member;
	}


}
