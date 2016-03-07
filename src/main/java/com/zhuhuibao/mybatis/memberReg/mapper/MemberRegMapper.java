package com.zhuhuibao.mybatis.memberReg.mapper;

import java.util.*;

import com.zhuhuibao.mybatis.memberReg.entity.Member;

public interface MemberRegMapper {
	//注册会员
	int registerMember(Member member);
	
	//更新邮箱验证码
	int updateEmailCode(Member member);
	
	//根据账号查询
	Member findMemberByAccount(Member member);
	
	//根据会员ID找到会员信息
	List<Member> findMemberByMail(String email);
	
	//会员更新密码
	int updateMemberPwd(Member member);
	
	//账号名是否存在
	int isExistAccount(Member member);
	
	//更新找回密码邮箱验证是否通过
	int updateMemberValidatePass(Member member);
	
}
