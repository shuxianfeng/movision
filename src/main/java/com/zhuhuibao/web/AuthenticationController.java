package com.zhuhuibao.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.product.service.ProductService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.security.resubmit.TokenHelper;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;
import com.zhuhuibao.utils.JsonUtils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录
 * @author caijl@20160303
 */
@Controller
public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
	private MemberRegService memberService;
    
    @RequestMapping(value = "/rest/web/authc", method = RequestMethod.GET)
    @ResponseBody
    public void isLogin(HttpServletRequest req,HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(200);
		Map<String, Object> map = new HashMap<String, Object>();

        if(null == session){
        	jsonResult.setMsgCode(0);
            jsonResult.setMessage("you are rejected!");
            map.put("authorized", false);
        }else{
        	ShiroUser principal = (ShiroUser)session.getAttribute("member");
        	if(null == principal){
            	jsonResult.setMsgCode(0);
                jsonResult.setMessage("you are rejected!");
                map.put("authorized", false);
        	}else{
        		LoginMember member = new LoginMember();
        		member.setAccount(principal.getAccount());
                member.setId(principal.getId());
            	jsonResult.setMsgCode(1);
                jsonResult.setMessage("welcome you!");
                map.put("authorized", true);
                map.put("member", member);
        	}
        }
        
        jsonResult.setData(map);
        response.setContentType("application/json;charset=utf-8");
      	response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
      	log.debug("caijl:/rest/web/authc is called,msgcode=["+jsonResult.getMsgCode()+"],Message=["+jsonResult.getMessage()+"].");
    }


    @RequestMapping(value="/rest/getToken",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getToken(HttpServletRequest req, HttpServletResponse rsp) {
        JsonResult result = new JsonResult();
        String  token = TokenHelper.setToken(req);
        result.setData(token);
        return result;
    }
    
    @RequestMapping(value="/rest/findMemberInfoById",method = RequestMethod.GET)
	@ResponseBody
	public void findMemberInfoById(HttpServletRequest req,HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException
	{
		JsonResult jsonResult = new JsonResult();
		Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null == session){
            jsonResult.setMessage("you are not login!");
        }
        else {
            ShiroUser principal = (ShiroUser) session.getAttribute("member");
            if (null != principal) {
                jsonResult = memberService.findMemberInfoById(principal.getId());
            }
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
    
    public static class LoginMember {
        private String account;
        private int id;
        private int ordercount;
        private int msgcount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
		public int getOrdercount() {
			return ordercount;
		}
		public void setOrdercount(int ordercount) {
			this.ordercount = ordercount;
		}
		public int getMsgcount() {
			return msgcount;
		}
		public void setMsgcount(int msgcount) {
			this.msgcount = msgcount;
		}

        /**
         * 重载equals,只计算account;
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            LoginMember other = (LoginMember) obj;

            if (account == null) {
                return false;
            } else if (account.equals(other.account))
                return true;
            return false;
        }
    }
}
