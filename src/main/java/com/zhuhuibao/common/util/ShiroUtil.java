package com.zhuhuibao.common.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.shiro.realm.ShiroRealm;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class ShiroUtil {
    private static Logger log = LoggerFactory.getLogger(ShiroUtil.class);
    public static Long  getCreateID()
    {
        Long createID = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
                if(principal != null)
                {
                    createID = principal.getId();
                }
            }
        }
        catch(Exception e)
        {
            log.error("get seesion user info error!",e);
        }
        return createID;
    }

    /**
     * 获取oms用户登陆ID
     * @return
     */
    public static Long  getOmsCreateID()
    {
        Long createID = null;
        try {
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
            	OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
                if(principal != null)
                {
                    createID = Long.valueOf(principal.getId()+"");
                }
            }
        }
        catch(Exception e)
        {
            log.error("get seesion user info error!",e);
        }
        return createID;
    }
}
