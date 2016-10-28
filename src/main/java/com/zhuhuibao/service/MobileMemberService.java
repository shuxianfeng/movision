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
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
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
     * 更新会员审核信息
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
     * 密码修改（参数全通过base64编码）
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return 错误提示消息（为空则表示修改成功）
     */
    public String updateMemberPwd(String oldPassword, String newPassword, String confirmPassword) {
        String errorMsg = "";
        if (isRightPassword(oldPassword)) {
            if ((StringUtils.trimToEmpty(newPassword)).equals(confirmPassword)) {
                String newPwd = new String(EncodeUtil.decodeBase64(newPassword));
                String md5Pwd = new Md5Hash(newPwd, null, 2).toString();
                Member member = new Member();
                member.setPassword(md5Pwd);
                member.setId(String.valueOf(ShiroUtil.getCreateID()));
                oldMemberService.updateMemInfo(member);

            } else {
                errorMsg = "两次输入的密码不一致";
            }
        } else {
            errorMsg = "密码输入错";
        }

        return errorMsg;
    }

    /**
     * 判断密码是当前登录者密码
     *
     * @param password
     * @return
     */
    private boolean isRightPassword(String password) {
        Member member = oldMemberService.findMemById(String.valueOf(ShiroUtil.getCreateID()));
        String md5Pwd = new Md5Hash(new String(EncodeUtil.decodeBase64(password)), null, 2).toString();

        return null != member && StringUtils.isNotBlank(member.getPassword()) ? member.getPassword().equals(md5Pwd) : false;
    }

    /**
     * 修改手机号码
     *
     * @param mobile
     * @param verifyCode
     * @return 错误提示消息（为空则表示修改成功）
     */
    public String updateMemberMobile(String mobile, String verifyCode) {
        String errorMsg = "";

        return errorMsg;
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

    /**
     * 根据会员ID查询会员信息
     */
    public Member findMemById(String id) {
        return memberMapper.findMemById(id);
    }

    /**
     * 获取企业荣誉资质信息
     *
     * @param id
     * @return
     */
    public List<CertificateRecord> certificateSearch(String id) {
        CertificateRecord certificateRecord = new CertificateRecord();
        certificateRecord.setMem_id(id);
        // 供应商资质
        certificateRecord.setType("1");
        certificateRecord.setIs_deleted(0);
        // 审核通过
        certificateRecord.setStatus("1");
        return oldMemberService.certificateSearch(certificateRecord);
    }

}
