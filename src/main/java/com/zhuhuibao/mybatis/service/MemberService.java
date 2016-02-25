package com.zhuhuibao.mybatis.service;

import com.zhuhuibao.mybatis.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.mapper.MemberMapper;

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
	public void updateMemBasicInfo(Member member)
	{
		log.debug("个人基本信息完善");
		String memId = member.getId().toString();
		Member mem = findMemberById(memId);

	}

	/**
	 * 根据ID查询会员信息
	 * @param memberId 会员id
	 * @return
	 */
	public Member findMemberById(String memberId)
	{
		log.debug("find memberinfo by memberId = "+memberId);
		return memberMapper.findMemById(memberId);
	}

}
