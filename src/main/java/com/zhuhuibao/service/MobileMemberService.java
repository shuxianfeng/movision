package com.zhuhuibao.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.service.impl.MembersService;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 会员中心业务处理实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileMemberService {

    private static final Logger log = LoggerFactory.getLogger(MobileMemberService.class);

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService oldMemberService;

    @Autowired
    private MembersService membersService;

    @Autowired
    private MemInfoCheckService memInfoCheckService;

    /**
     * 获取名企列表
     *
     * @return list
     */
    public Paging<Member> getGreatCompany(Paging<Member> pager, String identify) throws Exception {
        List<Member> members = memberMapper.findGreatCompanyByPager(pager.getRowBounds(), identify);
        pager.result(members);
        return pager;
    }

    /**
     * 获取名企详情信息
     *
     * @return list
     */
    public Map getGreatCompanyInfo(String id) throws Exception {
        return oldMemberService.introduce(id, "2");
    }

    /**
     * 搜索工程商
     *
     * @param spec
     *            查询条件
     * @return map
     * @throws ServiceException
     */
    public Map<String, Object> searchContractors(ContractorSearchSpec spec) throws ServiceException {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        return membersService.searchContractors(spec);
    }

    /**
     * 查询memberChk信息
     * 
     * @param id
     * @return
     */
    public MemInfoCheck getMemInfoCheckById(Long id) {
        try {
            return memInfoCheckService.findMemById(String.valueOf(id));
        } catch (Exception e) {
            log.error("执行异常>>", e);
            throw e;
        }
    }

    /**
     * <<<<<<< HEAD 更新会员审核信息
     * 
     * @param memInfoCheck
     */
    public void updateMemberInfoCheck(MemInfoCheck memInfoCheck) {
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        // 保证接口更新当前登录人数据
        memInfoCheck.setId(loginMember.getId());
        // 设置修改后的数据状态
        if (loginMember.getStatus() != MemberConstant.MemberStatus.WJH.intValue() || loginMember.getStatus() != MemberConstant.MemberStatus.ZX.intValue()) {
            memInfoCheck.setStatus(MemberConstant.MemberStatus.WSZLDSH.toString());
        }

        memInfoCheckService.update(memInfoCheck);
    }

    /**
     * 密码修改
     * 
     * @param oldPassword
     * @param newPassword
     * @param crmPassword
     */
    public void updateMemberPwd(String oldPassword, String newPassword, String crmPassword) {
        Map<String, String> map = new HashMap<>();

    }

    /**
     * 搜索供应商
     *
     * @param spec
     *            查询条件
     * @return map
     * @throws ServiceException
     */
    public Map<String, Object> searchSuppliers(SupplierSearchSpec spec) throws ServiceException {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        return membersService.searchSuppliers(spec);
    }
}
