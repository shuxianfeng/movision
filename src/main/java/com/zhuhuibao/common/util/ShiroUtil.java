package com.zhuhuibao.common.util;

import com.zhuhuibao.shiro.realm.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Created by Administrator on 2016/4/13 0013.
 */
public class ShiroUtil {

    public static Long  getCreateID()
    {
        Long createID = null;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            createID = principal.getId();
        }
        return createID;
    }
}
