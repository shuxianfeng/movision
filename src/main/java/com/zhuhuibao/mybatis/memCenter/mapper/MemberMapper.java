package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.AccountBean;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface MemberMapper {
    //根据会员ID找到会员信息
    Member findMemById(@Param("id") String id);

    //更新会员信息
    int updateMemInfo(Member member);

    //新建会员
    int addMember(Member member);

    /* 根据父类ID查询公司下属员工 */
    List<Member> findAllByPager(RowBounds rowBounds, Map<String, Object> map);

    List<Member> findStaffByParentId(Member member);

    //根据会员账号查询会员
    Member findMember(Member member);

    List<AccountBean> findAgentMember(@Param("account") String account,@Param("type") String type);

    //代理商邮件注册
    int agentRegister(Member member);

    List<Member> findNewEngineerOrSupplier(@Param("type") String type);

    List<ResultBean> findGreatCompany(@Param("type") String type);

    List<Member> findnewIdentifyEngineer(@Param("type") String type);

    List<OmsMemBean> findAllMemberByPager(RowBounds rowBounds,OmsMemBean member);

    List<OmsMemBean> findAllMemCertificateByPager(RowBounds rowBounds,OmsMemBean member);

    List<Map<String,String>> queryCompanyByKeywords(String keywords);

    List<Map<String,String>> queryCompanyList(Map<String, Object> map);

    List<Map<String,String>> queryGreatCompany(Map<String, Object> map);

    List<Map<String,String>> findMemberOms(Map<String, Object> map);

    List<Map<String,String>> queryEngineerList(Map<String, Object> map);
}