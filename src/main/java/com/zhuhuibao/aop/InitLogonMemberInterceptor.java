package com.zhuhuibao.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
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

	@Autowired
	private MemberRegService memberService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession(false);
		if (null != session) {
			ShiroRealm.ShiroUser member = (ShiroUser) session.getAttribute("member");
			if (null != member) {
				LoginMember loginMember = memberService.getLoginMemberByAccount(member.getAccount());
				if (null != loginMember) {
					member.setStatus(loginMember.getStatus());
					member.setVipLevel(loginMember.getVipLevel());
					member.setIsexpert(loginMember.getIsexpert());
					member.setWorkType(loginMember.getWorkType());
					member.setIdentify(loginMember.getIdentify());

					session.setAttribute("member", member);
				}
			}
		}

		return true;
	}
}
