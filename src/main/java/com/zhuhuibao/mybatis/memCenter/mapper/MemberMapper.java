package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.mybatis.memCenter.entity.Member;

import java.util.List;

public interface MemberMapper {
    //根据会员ID找到会员信息
    Member findMemById(String memberId);

    //更新会员信息
    int updateMemInfo(Member member);

    //新建会员
    int addMember(Member member);

    //修改会员
    int updateMember(Member member);

    //禁用会员
    int disableMember(Member member);

    //删除会员
    int deleteMember(Member member);

    //根据父类ID查询公司下属员工
    List<Member> findStaffByParentId(Member member);

    //根据会员账号查询会员
    Member findMem(Member member);

}