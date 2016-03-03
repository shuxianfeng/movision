package com.zhuhuibao.mybatis.memberReg.mapper;

import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;

public interface ValidateinfoMapper {
	//插入信息
	int insertValidateInfo(Validateinfo info);
	
	Validateinfo findMemberValidateInfo(Validateinfo info);
	
	int updateValidateInfo(Validateinfo info);
	
	int deleteValidateInfo(Validateinfo info);
	
}
