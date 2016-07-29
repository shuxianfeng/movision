package com.zhuhuibao.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
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

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (null != session) {
				ShiroRealm.ShiroUser member = (ShiroUser) session.getAttribute("member");
				if (null != member) {
					LoginMember loginMember = memberService.getLoginMemberByAccount(member.getAccount());
					if (null != loginMember) {

						member = new ShiroUser(loginMember.getId(), loginMember.getAccount(), loginMember.getStatus(),
								loginMember.getIdentify(), loginMember.getRole(), loginMember.getIsexpert(),
								loginMember.getCompanyId(), loginMember.getRegisterTime(), loginMember.getWorkType(),
								loginMember.getHeadShot(), loginMember.getNickname(), loginMember.getCompanyName(),
								loginMember.getVipLevel());

						session.setAttribute("member", member);
					}
				}
			}
		} catch (Exception e) {
			log.error("InitLogonMemberInterceptor error", e);
		}

		return true;
	}
}
