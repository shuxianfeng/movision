package com.zhuhuibao.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.zhuhuibao.mybatis.memberReg.entity.LoginMember;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;

/**
 * 
 * @author tongxl
 *
 */
public class InitLogonMemberInterceptor extends HandlerInterceptorAdapter {
    private Logger log = LoggerFactory.getLogger(InitLogonMemberInterceptor.class);

    @Autowired
    private MemberRegService memberService;

    @Autowired
    private ShiroRealm shiroRealm;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (null != session) {
                // 获取当前会话登录人
                ShiroRealm.ShiroUser member = (ShiroUser) session.getAttribute("member");
                if (null != member) {
                    LoginMember loginMember = memberService.getLoginMemberByAccount(member.getAccount());
                    if (null != loginMember) {
                        // 初始化登录信息
                        ShiroRealm.ShiroUser loginInfo = new ShiroUser(loginMember.getId(), loginMember.getAccount(), loginMember.getStatus(), loginMember.getIdentify(), loginMember.getRole(),
                                loginMember.getIsexpert(), loginMember.getCompanyId(), loginMember.getRegisterTime(), loginMember.getWorkType(), loginMember.getHeadShot(), loginMember.getNickname(),
                                loginMember.getCompanyName(), loginMember.getVipLevel(), loginMember.getEnterpriseLinkman(), loginMember.getFixedTelephone(), loginMember.getEmail());

                        if (loginMemberInfoIsChange(member, loginInfo)) {
                            session.setAttribute("member", loginInfo);
                            shiroRealm.getAuthorizationCache().remove(subject.getPrincipals());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("InitLogonMemberInterceptor error", e);
        }

        return true;
    }

    /**
     * 判断登录信息是否改变
     * 
     * @param member
     * @param loginInfo
     * @return
     */
    private boolean loginMemberInfoIsChange(ShiroUser member, ShiroRealm.ShiroUser loginInfo) {
        boolean isChange = member == null || loginInfo == null || loginInfo.getWorkType() != member.getWorkType() || loginInfo.getVipLevel() != member.getVipLevel()
                || loginInfo.getStatus() != member.getStatus() || !loginInfo.getIdentify().equals(member.getIdentify()) || !loginInfo.getRole().equals(member.getRole())
                || !loginInfo.getIsexpert().equals(member.getIsexpert());
        return isChange;
    }
}
