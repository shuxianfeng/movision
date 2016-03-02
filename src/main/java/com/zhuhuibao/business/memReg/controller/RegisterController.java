package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.memberReg.service.RegisterValidateService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.ResourcePropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.sms.SDKSendTemplateSMS;

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
	
	@Autowired
	private DictionaryService ds;
	
	/**
	 * 注册验证码业务类
	 */
	@Autowired
	private RegisterValidateService rvService;
	
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
		SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		Validateinfo info = new Validateinfo();
		info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		info.setCheckCode(verifyCode);
		info.setAccount(mobile);
		memberService.inserValidateInfo(info);
		sess.setAttribute("r"+mobile, verifyCode);
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
		String mobile = req.getParameter("mobile");
		log.debug("获得手机验证码  mobile=="+mobile);
		HttpSession sess = req.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
		log.debug("verifyCode == " + verifyCode);
		//发送验证码到手机
		SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		Validateinfo info = new Validateinfo();
		info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		info.setCheckCode(verifyCode);
		info.setAccount(mobile);
		memberService.inserValidateInfo(info);
		sess.setAttribute("s"+mobile, verifyCode);
		JsonResult jsonResult = new JsonResult();
		jsonResult.setData(verifyCode);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
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
			String verifyCode = (String) req.getSession().getAttribute("r"+member.getMobile());
			Validateinfo info = new Validateinfo();
			info.setAccount(member.getMobile());
			info.setValid(0);
			info.setCheckCode(verifyCode);
			info = memberService.findMemberValidateInfo(info);
			Date currentTime = new Date();
			Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"),12,10);
			if(currentTime.before(sendSMStime)) 
			{ 
				if(info != null &&  info.getCheckCode().equalsIgnoreCase(member.getMobileCheckCode()))
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
						result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);
					}
				}
				else
				{
					result.setCode(400);
					result.setMessage("验证码不正确");
					result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
				}
				log.debug("mobile verifyCode == " + member.getMobileCheckCode());
			}
			else
			{
				result.setCode(400);
				result.setMessage("短信验证超时");
				result.setMsgCode(MsgCodeConstant.member_mcode_sms_timeout);
			}
		}
		if(member.getEmailCheckCode() != null )
		{
			String verifyCode = (String) req.getSession().getAttribute("email");
			if(verifyCode != null && verifyCode.equalsIgnoreCase(member.getEmailCheckCode()) )			{
				if(member.getEmail().indexOf("@")>=0)
				{
					int isExist = memberService.isExistAccount(member);
					if(isExist == 0)
					{
						memberService.registerMember(member);
						//发送激活链接给此邮件
						rvService.sendMailActivateCode(member,ResourcePropertiesUtils.getValue("host.ip"));
						//是否显示“立即激活按钮”
						String mail = ds.findMailAddress(member.getEmail());
						Map<String,String> map = new HashMap<String,String>();
						if(mail != null && !mail.equals(""))
						{
							map.put("button", "true");
						}
						else
						{
							map.put("button", "false");
						}
						result.setData(map);
					}
					else
					{
						result.setCode(400);
						result.setMessage("账户名已经存在");
						result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);
					}
				}
			}
			else
			{
				result.setCode(400);
				result.setMessage("邮件验证码不正确");
				result.setMsgCode(MsgCodeConstant.member_mcode_mail_validate_error);
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
		String seekMobileCode = (String) req.getSession().getAttribute("s"+member.getMobile());
		Validateinfo info = new Validateinfo();
		info.setAccount(member.getMobile());
		info.setValid(0);
		info.setCheckCode(seekMobileCode);
		info = memberService.findMemberValidateInfo(info);
		Date currentTime = new Date();
		Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"),12,10);
		if(currentTime.before(sendSMStime)) 
		{
			if(info != null)
			{
				if(member.getMobileCheckCode() == null || !member.getMobileCheckCode().equals(info.getCheckCode()))
				{
					result.setCode(400);
					result.setMessage("手机验证码错误");
					result.setData(member.getMobile());
					result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
				}
			}
		}
		else
		{
			result.setCode(400);
			result.setMessage("短信验证超时");
			result.setMsgCode(MsgCodeConstant.member_mcode_sms_timeout);
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
		rvService.sendValidateMail(member,ResourcePropertiesUtils.getValue("host.ip"));
		String mail = ds.findMailAddress(member.getEmail());
		Map<String,String> map = new HashMap<String,String>();
		if(mail != null && !mail.equals(""))
		{
			map.put("button", "true");
		}
		else
		{
			map.put("button", "false");
		}
		result.setData(map);
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
	@RequestMapping(value = "/rest/activateEmail", method = RequestMethod.GET)
	public ModelAndView activateEmail(HttpServletRequest req, Model model) throws UnsupportedEncodingException {
		log.debug("email activate start.....");
		JsonResult jsonResult = new JsonResult();
		ModelAndView modelAndView = new ModelAndView(); 
		try
        {
			String vm = req.getParameter("vm");//获取email
			if(vm != null & !vm.equals(""))
			{
				String decodeVM = new String (EncodeUtil.decodeBase64(vm));
	        	jsonResult = rvService.processActivate(decodeVM);
	        	 modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(String.valueOf(jsonResult.getData()).getBytes())); 
	        	if(jsonResult.getCode() == 200)
	        	{
	        		//跳转到会员中心页面
	        		RedirectView rv = new RedirectView("http://"+ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("active.mail.page"));
	        		modelAndView.setView(rv);
	        	}
	        	else
	        	{
	        		RedirectView rv = new RedirectView("http://"+ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("active.mail.replay.page"));
	    	        modelAndView.setView(rv);
	        	}
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
	@RequestMapping(value = "/rest/validateMail", method = RequestMethod.GET)
	public ModelAndView validateMail(HttpServletRequest req, Model model) throws UnsupportedEncodingException {
		log.debug("validate mail start.....");
		String vm = req.getParameter("vm");//获取email
		 ModelAndView modelAndView = new ModelAndView();  
		if(vm != null & !vm.equals(""))
		{
			RedirectView rv = null;
			JsonResult jsonResult = new JsonResult();
	        try
	        {
	        	jsonResult = rvService.processValidate(vm);
	        	String email = (String) jsonResult.getData();
	        	modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(email.getBytes()));  
	        	if(jsonResult.getCode() == 200)
	 	        {
	 		        rv = new RedirectView("http://"+ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("reset.pwd.page"));
	 	        }
	 	        else
	 	        {
	 	        	rv = new RedirectView("http://"+ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("reset.pwd.invalid.page"));
	 	        }
	 	        modelAndView.setView(rv);
	        }
	        catch(Exception e)
	        {
	        	log.error("email activate member error!",e);
	        }
		}
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
		String account = req.getParameter("account");
		int result = memberService.isValidatePass(EncodeUtil.decodeBase64ToString(account));
		jsonResult.setData(result);
		response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
	}
	
	/**
	 * 查看激活邮件
	 * @param req
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/watchMail", method = RequestMethod.GET)
	@ResponseBody
	public void watchMail(HttpServletRequest req,HttpServletResponse response) throws IOException {
		log.debug("找回密码是否验证");
		JsonResult jsonResult = new JsonResult();
		String account = req.getParameter("email");
		String result = ds.findMailAddress(EncodeUtil.decodeBase64ToString(account));
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
