package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.memberReg.service.RegisterValidateService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;

/**
 * @author jianglz
 * @since 15/12/12.
 */
@Controller
public class RegisterController {
	private static final Logger log = LoggerFactory
			.getLogger(RegisterController.class);

	@Autowired
	private MemberRegService memberService;
	
	/**
	 * 注册验证码业务类
	 */
	@Autowired
	private RegisterValidateService rvService;
	
	@Autowired
    private HttpServletRequest request;
	
	 /**
	  * 邮箱注册时的图形验证码
	  * @param req
	  * @param response
	  * @param model
	  */
	@RequestMapping(value = "/rest/imgCode", method = RequestMethod.GET)
	public void getCode(HttpServletRequest req, HttpServletResponse response,
			Model model) {
		log.debug("获得验证码");
//		getImageVerifyCode(req, response,100,40,4,"email");
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
		sess.setAttribute("email", verifyCode);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			int w = 100;// 定义图片的width
			int h = 40;// 定义图片的height
			VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
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
	
	/**
	  * 找回密码的图形验证码
	  * @param req
	  * @param response
	  * @param model
	  */
	@RequestMapping(value = "/rest/getSeekPwdImgCode", method = RequestMethod.GET)
	public void getSeekPwdImgCode(HttpServletRequest req, HttpServletResponse response,
			Model model) {
		log.debug("找回密码的图形验证码");
//		getImageVerifyCode(req, response,100,40,4,"seekPwdCode");
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
		sess.setAttribute("seekPwdCode", verifyCode);
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			int w = 100;// 定义图片的width
			int h = 40;// 定义图片的height
			VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
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
	
	/**
	 * 手机注册账号时发送的验证码
	 * @param req
	 * @param response
	 * @param model
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value = "/rest/mobileCode", method = RequestMethod.GET)
	public void getMobileCode(HttpServletRequest req, HttpServletResponse response,
			Model model) throws JsonGenerationException, JsonMappingException, IOException {
		String mobile = req.getParameter("mobile");
		log.debug("获得手机验证码  mobile=="+mobile);
		HttpSession sess = req.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
		log.debug("verifyCode == " + verifyCode);
		//发送验证码到手机
//		SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		sess.setAttribute("mobile", verifyCode);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(verifyCode);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	/**
	 * 找回密码时手机发送的验证码
	 * @param req
	 * @param response
	 * @param model
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	@RequestMapping(value = "/rest/getSeekPwdMobileCode", method = RequestMethod.GET)
	public void getSeekPwdMobileCode(HttpServletRequest req, HttpServletResponse response,
			Model model) throws JsonGenerationException, JsonMappingException, IOException {
		String mobile = req.getParameter("seekmobile");
		log.debug("获得手机验证码  mobile=="+mobile);
		HttpSession sess = req.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
		log.debug("verifyCode == " + verifyCode);
		//发送验证码到手机
//		SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		sess.setAttribute("seekPwdMobile", verifyCode);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(verifyCode);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}

	@RequestMapping(value = "/checkCode")
	@ResponseBody
	public void checkCode(HttpServletRequest req, HttpServletResponse response) throws IOException {
		/*
		 * System.out.println("要校验的验证码:"+user); return new
		 * Gson().toJson("true");
		 */
		response.setContentType("application/json;charset=utf-8");
		String jsonData = "{\"result\":\"ok\"}";
		response.getWriter().write(jsonData);
		/*ModelAndView modelAndView = new ModelAndView();  
        modelAndView.addObject("name", "xxx");  
//        modelAndView.setViewName("redirect:/register");  
        modelAndView.setViewName("/register");  
        return modelAndView;*/
	}

    /**
     * 会员简单注册
     * @param req
     * @param member
     * @param model
     * @return
     * @throws IOException 
     */
	@RequestMapping(value = "/rest/register", method = RequestMethod.POST)
	@ResponseBody
	public void register(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException {
		log.debug("注册  mobile=="+member.getMobile()+" email =="+member.getEmail());
		JsonResult result = new JsonResult();
		//校验手机验证码是否正确
		if(member.getMobileCheckCode() != null )
		{
			String verifyCode = (String) req.getSession().getAttribute("mobile");
			if(verifyCode != null && verifyCode.equalsIgnoreCase(member.getMobileCheckCode()))
			{
				int isExist = memberService.isExistAccount(member);
				if(isExist == 0)
				{
					memberService.registerMember(member);
				}
				else
				{
					result.setCode(400);
					result.setMessage("账户名已经存在");
				}
			}
			else
			{
				result.setCode(400);
				result.setMessage("验证码不正确");
			}
			log.debug("mobile verifyCode == " + member.getMobileCheckCode());
		}
		if(member.getEmailCheckCode() != null )
		{
			String verifyCode = (String) req.getSession().getAttribute("email");
			if(verifyCode != null && verifyCode.equalsIgnoreCase(member.getEmailCheckCode()))
			{
				int isExist = memberService.isExistAccount(member);
				if(isExist == 0)
				{
					member.setEmailCheckCode(verifyCode);
					memberService.registerMember(member);
					if(member.getEmail()!=null && member.getEmail().indexOf("@")>0)
					{
						//发送激活链接给此邮件
						rvService.sendMailActivateCode(member);
					}
				}
				else
				{
					result.setCode(400);
					result.setMessage("账户名已经存在");
				}
			}
			else
			{
				result.setCode(400);
				result.setMessage("验证码不正确");
			}
			log.debug("email verifyCode == " + member.getEmailCheckCode());
		}
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}
	
	 /**
     * 找回密码时填写账户名
     * @param req
     * @param member
     * @param model
     * @return
     * @throws IOException 
     */
	@RequestMapping(value = "/rest/writeAccount", method = RequestMethod.POST)
	@ResponseBody
	public void writeAccount(HttpServletRequest req,HttpServletResponse response,Member member,Model model) throws IOException {
		log.debug("seek pwd write account");
		JsonResult result = new JsonResult();
		String seekCode = (String) req.getSession().getAttribute("seekPwdCode");
		log.info("writeAccount seekCode === "+seekCode);
		result = memberService.writeAccount(member, seekCode);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	} 
	
	/**
	 * 手机验证身份
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/mobileValidate", method = RequestMethod.POST)
	@ResponseBody
	public void mobileValidate(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException {
		log.debug("找回密码  mobile =="+member.getMobile());
		JsonResult result = new JsonResult();
		String seekMobileCode = (String) request.getSession().getAttribute("seekPwdMobile");
		if(member.getMobileCheckCode() == null || !member.getMobileCheckCode().equals(seekMobileCode))
		{
			result.setCode(400);
			result.setMessage("手机验证码错误");
			result.setData(member.getMobile());
		}
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}
	
	/**
	 * 发送验证邮件密码重置
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/sendValidateMail", method = RequestMethod.POST)
	@ResponseBody
	public void sendValidateMail(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException {
		log.debug("找回密码  email =="+member.getEmail());
		JsonResult result = new JsonResult();
		rvService.sendValidateMail(member);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
	}
	
	/**
	 * 修改密码
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public void modifyPwd(HttpServletRequest req,HttpServletResponse response, Member member,Model model) throws IOException {
		log.debug("重置密码");
		JsonResult jsonResult = new JsonResult();
		int result = memberService.modifyPwd(member);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	/**
	 * 邮件激活账户
	 * @param req
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/activateEmail", method = RequestMethod.GET)
	public ModelAndView activateEmail(HttpServletRequest req, Model model) throws UnsupportedEncodingException {
		log.debug("email activate start.....");
		JsonResult jsonResult = new JsonResult();
		ModelAndView modelAndView = new ModelAndView(); 
		try
        {
			String vm = request.getParameter("vm");//获取email
			String decodeVM = new String (EncodeUtil.decodeBase64(vm));
        	jsonResult = rvService.processActivate(decodeVM);
        	 modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(String.valueOf(jsonResult.getData()).getBytes())); 
        	if(jsonResult.getCode() == 200)
        	{
        		//跳转到会员中心页面
        		RedirectView rv = new RedirectView("http://localhost:1234/forgot.html");
        		modelAndView.setView(rv);
        	}
        	else
        	{
    	        RedirectView rv = new RedirectView("http://localhost:1234/forgot.html");
    	        modelAndView.setView(rv);
        	}
        	
        }
        catch(Exception e)
        {
        	log.error("email activate member error!",e);
        }
       
        return modelAndView; 
	}
	
	/**
	 * 点击重置密码邮件链接验证身份
	 * @param req
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/validateMail", method = RequestMethod.GET)
	public ModelAndView validateMail(HttpServletRequest req, Model model) throws UnsupportedEncodingException {
		log.debug("validate mail start.....");
		JsonResult jsonResult = new JsonResult();
		String action = request.getParameter("action");
		String vm = request.getParameter("vm");//获取email
		String email = new String (EncodeUtil.decodeBase64(vm));
        try
        {
        	jsonResult = rvService.processValidate(email);
        }
        catch(Exception e)
        {
        	log.error("email activate member error!",e);
        }
        ModelAndView modelAndView = new ModelAndView();  
        modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(email.getBytes()));  
        RedirectView rv = new RedirectView("http://localhost:1234/forgot.html");
        modelAndView.setView(rv);
        return modelAndView;
	}
	
	/**
	 * 找回密码是否验证通过
	 * @param req
	 * @param response
	 * @param member
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/isValidatePass", method = RequestMethod.GET)
	@ResponseBody
	public void isValidatePass(HttpServletRequest req,HttpServletResponse response) throws IOException {
		log.debug("找回密码是否验证");
		JsonResult jsonResult = new JsonResult();
		String account = request.getParameter("account");
		int result = memberService.isValidatePass(account);
		jsonResult.setData(result);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
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
			int w = 100;// 定义图片的width
			int h = 40;// 定义图片的height
			VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
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
