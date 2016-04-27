package com.zhuhuibao.web;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.oms.service.UserService;
import com.zhuhuibao.security.resubmit.TokenHelper;
import com.zhuhuibao.shiro.realm.OMSRealm;
import com.zhuhuibao.utils.JsonUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 * @author caijl@20160303
 */
@Controller
public class OMSAuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(OMSAuthenticationController.class);

    @Autowired
	private UserService userService;
    
    @RequestMapping(value = "/rest/oms/authc", method = RequestMethod.GET)
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
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser)session.getAttribute("oms");
        	if(null == principal){
            	jsonResult.setMsgCode(0);
                jsonResult.setMessage("you are rejected!");
                map.put("authorized", false);
        	}else{
                LoginUser user = new LoginUser();
                user.setAccount(principal.getAccount());
                user.setId(principal.getId());
            	jsonResult.setMsgCode(1);
                jsonResult.setMessage("welcome you!");
                map.put("authorized", true);
                map.put("user", user);
        	}
        }
        
        jsonResult.setData(map);
        response.setContentType("application/json;charset=utf-8");
      	response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
      	log.debug("caijl:/rest/web/authc is called,msgcode=["+jsonResult.getMsgCode()+"],Message=["+jsonResult.getMessage()+"].");
    }


    @RequestMapping(value="/rest/oms/getToken",method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getToken(HttpServletRequest req, HttpServletResponse rsp) {
        JsonResult result = new JsonResult();
        String  token = TokenHelper.setToken(req);
        result.setData(token);
        return result;
    }
    
    @RequestMapping(value="/rest/oms/findMemberInfoById",method = RequestMethod.GET)
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
            OMSRealm.ShiroOmsUser principal = (OMSRealm.ShiroOmsUser) session.getAttribute("oms");
            if (null != principal) {
                jsonResult = userService.selectByPrimaryKey(principal.getId());
            }
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
    
    public static class LoginUser {
        private String account;
        private Integer id;

        public Integer getId() {
            return id;
        }
        public void setId(Integer id) {
            this.id = id;
        }
        public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
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
            LoginUser other = (LoginUser) obj;

            if (account == null) {
                return false;
            } else if (account.equals(other.account))
                return true;
            return false;
        }
    }
}
