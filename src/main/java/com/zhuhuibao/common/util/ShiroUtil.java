package com.zhuhuibao.common.util;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm;

public class ShiroUtil {
	private static Logger log = LoggerFactory.getLogger(ShiroUtil.class);

	public static ShiroRealm.ShiroUser getMember() {
		ShiroRealm.ShiroUser member = null;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession(false);
			if (session != null) {
				member = (ShiroRealm.ShiroUser) session.getAttribute("member");
			}
		} catch (Exception e) {
			log.error("get seesion user info error!", e);
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

		}
		return member;

	}

	public static Long getCreateID() {
		Long createID = null;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession(false);
			if (session != null) {
				ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
				if (principal != null) {
					createID = principal.getId();
				}
			}
		} catch (Exception e) {
			log.error("get seesion user info error!", e);
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

		}
		return createID;
	}

	public static Long getCompanyID() {
		Long companyId = null;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession(false);
			if (session != null) {
				ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
				if (principal != null) {
					companyId = principal.getCompanyId();
				}
			}
			return companyId;
		} catch (Exception e) {
			log.error("get seesion user info error!", e);
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
	}

	/**
	 * 获取oms用户登陆ID
	 *
	 * @return
	 */
	public static Long getOmsCreateID() {
		Long createID = null;
		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session session = currentUser.getSession(false);
			if (session != null) {
				OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
				if (principal != null) {
					createID = Long.valueOf(principal.getId() + "");
				}
			}
		} catch (Exception e) {
			log.error("get seesion user info error!", e);
			throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
		}
		return createID;
	}
}
