package com.zhuhuibao.mybatis.mapper;

import com.zhuhuibao.mybatis.entity.Member;

public interface MemberMapper {
    //根据会员ID找到会员信息
    Member findMemById(String memberId);
}