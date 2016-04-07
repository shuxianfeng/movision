package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.AccountBean;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

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
    //int disableMember(Member member);

    //删除会员
    int deleteMember(String id);

    /* 根据父类ID查询公司下属员工 */
    List<Member> findAllByPager(RowBounds rowBounds, Member member);

    List<Member> findStaffByParentId(Member member);

    //根据会员账号查询会员
    Member findMem(Member member);

    Member findMemer(Member member);

    //重置密码
    int resetPwd(Member member);

    int uploadHeadShot(Member member);

    int uploadLogo(Member member);

    List<AccountBean> findAgentMember(@Param("account") String account,@Param("type") String type);

    //代理商邮件注册
    int agentRegister(Member member);
}