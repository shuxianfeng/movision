package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.mybatis.memCenter.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;

import java.io.IOException;

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
	 * 会员信息保存
	 */
	public int updateMemInfo(Member member)
	{
		log.debug("会员信息保存");
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

	/**
	 * 新建员工
	 */
	public int addMember(Member member)
	{
		log.debug("新建员工");
		int result = 0;
		result = memberMapper.addMember(member);
		return result;
	}

	/**
	 * 修改员工
	 */
	public int updateMember(Member member)
	{
		log.debug("修改员工");
		int result = 0;
		result = memberMapper.updateMember(member);
		return result;
	}

	/**
	 * 禁用员工
	 */
	public int disableMember(Member member)
	{
		log.debug("修改员工");
		int result = 0;
		result = memberMapper.disableMember(member);
		return result;
	}

	/**
	 * 删除员工
	 */
	public int deleteMember(Member member)
	{
		log.debug("修改员工");
		int result = 0;
		result = memberMapper.deleteMember(member);
		return result;
	}

	/**
	 * 员工密码重置
	 */
	public int resetPwd(Member member)
	{
		log.debug("密码重置");
		int result = 0;
		result = memberMapper.resetPwd(member);
		return result;
	}
}
