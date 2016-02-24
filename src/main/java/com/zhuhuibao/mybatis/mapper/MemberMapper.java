package com.zhuhuibao.mybatis.mapper;

import com.zhuhuibao.mybatis.entity.member.Member;

public interface MemberMapper {
	//注册会员
	int registerMember(Member member);
	
	//完善会员基本信息
	int addMemberBaseInfo(Member member);
	
	//根据账号查询
	Member findMemberByAccount(Member member);
	
	//根据会员ID找到会员信息
	Member findMemberById(String memberId);
	
	//更新会员注册状态
	int updateMemberStatus(Member member);
	
	//手机账号会员更新密码
	int updateMemberPwdByMobile(Member member);
	
	//邮箱账号会员更新密码
	int updateMemberPwdByMail(Member member);
	
	//找回密码是否验证通过
	Integer isValidatePass(String account);
}
