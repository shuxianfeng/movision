package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Member;

public interface MemberMapper {
    //根据会员ID找到会员信息
    Member findMemById(String memberId);

    //更新会员信息
    int updateMemInfo(Member member);
}