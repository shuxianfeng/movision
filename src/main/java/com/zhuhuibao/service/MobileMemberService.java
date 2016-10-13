package com.zhuhuibao.service;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.service.impl.MembersService;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
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
    public List<Member> getGreatCompany(Paging<Member> pager) throws Exception {
        return memberMapper.findGreatCompanyByPager(pager.getRowBounds());
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
}
