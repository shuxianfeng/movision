package com.zhuhuibao.business.oms.member;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 运营管理系统登录
 * @author penglong
 *
 */
@RestController
public class OmsLoginController {
	private static final Logger log = LoggerFactory.getLogger(OmsLoginController.class);
	
	@RequestMapping(value = "/rest/oms/login", method = RequestMethod.POST)
	@ApiOperation(value = "运营登录", notes = "运营登录", response = Response.class)
    public Response login(@ApiParam(value = "用户名") @RequestParam String username,
            @ApiParam(value = "密码（Base64加密）") @RequestParam String password) throws IOException {
        log.info("oms login post 登录校验");
        Response jsonResult = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        //String username = "";
        UsernamePasswordToken token = null;
        try {
//        	String verifyCode = (String) req.getSession().getAttribute("oms");
//			if(verifyCode != null && verifyCode.equalsIgnoreCase(member.getCheckCode()))
//        	{
				//username = user.getUsername();
		        //String pwd = new String(EncodeUtil.decodeBase64(user.getPassword()));
        		String pwd = new String(EncodeUtil.decodeBase64(password));
				//user.setPassword(pwd);
		        token = new UsernamePasswordToken(username, pwd);
		        currentUser.login(token);
		        jsonResult.setData(username);
//        	}
			/*else
			{
				jsonResult.setCode(400);
				jsonResult.setMessage("验证码错误");
			}*/
        } catch (UnknownAccountException e) {
//            e.printStackTrace();
            jsonResult.setCode(400);
            jsonResult.setMessage("用户名不存在");
        }catch (LockedAccountException e){
            /*e.printStackTrace();
            model.addAttribute("error", "帐户状态异常");
            result = "login";*/
		    jsonResult.setCode(400);
		    jsonResult.setMessage("帐户状态异常");
        } catch (AuthenticationException e){
            /*e.printStackTrace();
            model.addAttribute("error", "用户名或密码错误");
            result = "login";*/
        	jsonResult.setCode(400);
		    jsonResult.setMessage("用户名或密码错误");
        }

        if(currentUser.isAuthenticated()){
			Session session = currentUser.getSession();
			session.setAttribute("oms", currentUser.getPrincipal());
            System.out.println("oms 用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
        }else{
            token.clear();
        }
        /*response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));*/
        return jsonResult;
    }
    
    @RequestMapping(value = "/rest/oms/logout", method = RequestMethod.GET)
    public void logout(HttpServletResponse response) throws IOException
    {
        SecurityUtils.getSubject().logout();
        Response jsonResult = new Response();
        response.setContentType("application/json;charset=utf-8");
      	response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
    
    /**
	  * 获得图形验证码
	  * @param req
	  * @param response
	 */
	@RequestMapping(value = "/rest/oms/imgCode", method = RequestMethod.GET)
	public void getCode(HttpServletRequest req, HttpServletResponse response) {
		log.debug("获得验证码");
		getImageVerifyCode(req, response,100,40,4,"oms");
	}
    
    /**
	 * 生成图片验证码
	 * @param req
	 * @param response
	 * @param imgWidth  图片的宽度
	 * @param imgheight 图片的高度
	 * @param verifySize 验证码的长度
	 * @param key session存储的关键字
	 */
	private void getImageVerifyCode(HttpServletRequest req,
			HttpServletResponse response,int imgWidth,int imgheight,int verifySize,String key) {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		HttpSession sess = req.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		log.debug("verifyCode == " + verifyCode);
		sess.setAttribute(key, verifyCode);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			VerifyCodeUtils.outputImage1(imgWidth, imgheight, out, verifyCode);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
