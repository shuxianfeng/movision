package com.zhuhuibao.web;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MessageLogConstant;
import com.zhuhuibao.common.pojo.AuthcMember;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.security.resubmit.TokenHelper;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @author caijl@20160303
 */
@RestController
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private MemberRegService memberService;

    @Autowired
    SiteMailService siteMailService;


    @RequestMapping(value = "/rest/web/authc", method = RequestMethod.GET)
    public Response isLogin() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        Response response = new Response();
        response.setCode(200);
        Map<String, Object> map = new HashMap<String, Object>();

        if (null == session) {
            response.setMsgCode(0);
            response.setMessage("you are rejected!");
            map.put("authorized", false);
        } else {
            ShiroUser member = (ShiroUser) session.getAttribute("member");
            if (null == member) {
                response.setMsgCode(0);
                response.setMessage("you are rejected!");
                map.put("authorized", false);
            } else {
                String identity = member.getIdentify();
                String role = member.getRole();
                String isexpert = member.getIsexpert();
                boolean bexpert = false;
                if (identity.equals("2")) {
                    if (isexpert.equals("1")) {
                        bexpert = true;
                    }
                } else {
                    if (identity.length() > 1) {
                        String[] strs = identity.split(",");
                        if (Arrays.asList(strs).contains("3")) {
                            identity = "3,1";
                        } else {
                            identity = "1";
                        }
                    } else if (!identity.equals("3")) {
                        identity = "1";
                    }

                    if (!role.equals("100")) {
                        role = "300";
                    }
                }
                AuthcMember authcMember = new AuthcMember();
                authcMember.setId(member.getId());
                authcMember.setAccount(member.getAccount());
                authcMember.setCompanyId(member.getCompanyId());
                authcMember.setStatus(member.getStatus());
                authcMember.setIdentify(identity);
                authcMember.setRole(role);
                authcMember.setIsexpert(bexpert);
                authcMember.setVipLevel(member.getVipLevel());

                authcMember.setRegisterTime(member.getRegisterTime());
                authcMember.setWorkType(member.getWorkType());
                authcMember.setHeadShot(member.getHeadShot());

                Map<String, Object> nMap = new HashMap<>();
                nMap.put("recID", String.valueOf(member.getId()));
                nMap.put("status", MessageLogConstant.NEWS_STATUS_ONE);
                int count = siteMailService.selUnreadNewsCount(nMap);

                authcMember.setMsgCount(count);
                authcMember.setNickname(member.getNickname());
                authcMember.setCompanyName(member.getCompanyName());


                response.setMsgCode(1);
                response.setMessage("welcome you!");
                map.put("authorized", true);
                map.put("member", authcMember);

            }
        }

    response.setData(map);
    log.debug("caijl:/rest/web/authc is called,msgcode=["+response.getMsgCode()+"],Message=["+response.getMessage()+"].");
    return response;

}


    @RequestMapping(value = "/rest/getToken", method = RequestMethod.GET)
    public Response getToken(HttpServletRequest req, HttpServletResponse rsp) {
        Response result = new Response();
        String token = TokenHelper.setToken(req);
        result.setData(token);
        return result;
    }

    @RequestMapping(value = "/rest/findMemberInfoById", method = RequestMethod.GET)
    public Response findMemberInfoById() throws IOException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null == session) {
            response.setMessage("you are not login!");
        } else {
            ShiroUser principal = (ShiroUser) session.getAttribute("member");
            if (null != principal) {
                response = memberService.findMemberInfoById(principal.getId());
            }
        }

        return response;
    }

}
