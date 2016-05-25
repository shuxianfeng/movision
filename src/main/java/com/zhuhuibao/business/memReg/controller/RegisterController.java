package com.zhuhuibao.business.memReg.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.zhuhuibao.common.Response;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.taobao.api.ApiException;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.memberReg.service.RegisterValidateService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.sms.SDKSendTaoBaoSMS;

/**
 * @author jianglz
 * @since 15/12/12.
 */
@RestController
public class RegisterController {
	private static final Logger log = LoggerFactory.getLogger(RegisterController.class);

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
	  * @param response
	  */
	@RequestMapping(value = "/rest/imgCode", method = RequestMethod.GET)
	public void getCode(HttpServletResponse response) {
		log.debug("获得验证码");
//		getImageVerifyCode(req, response,100,40,4,"email");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		log.debug("verifyCode == " + verifyCode);
		sess.setAttribute("email", verifyCode);
		
		ServletOutputStream out = null;
		genImgCode(response, verifyCode, out);
	}

	/**
	 * 生成图片码
	 * @param response
	 * @param verifyCode
	 * @param out
     */
	private void genImgCode(HttpServletResponse response, String verifyCode, ServletOutputStream out) {
		try {
			out = response.getOutputStream();
			int w = 100;// 定义图片的width
			int h = 40;// 定义图片的height
			VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
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
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
		log.debug("verifyCode == " + verifyCode);
		sess.setAttribute("seekPwdCode", verifyCode);
		ServletOutputStream out = null;
		genImgCode(response, verifyCode, out);
	}
	
	/**
	 * 手机注册账号时发送的验证码
	 * @param req
	 * @throws IOException
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @throws ApiException 
	 */
	@RequestMapping(value = "/rest/mobileCode", method = RequestMethod.GET)
	public Response getMobileCode(HttpServletRequest req) throws IOException, ApiException {
		String mobile = req.getParameter("mobile");
		log.debug("获得手机验证码  mobile=="+mobile);
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
		log.debug("verifyCode == " + verifyCode);
		//发送验证码到手机
		//SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		SDKSendTaoBaoSMS.sendRegisterSMS(mobile, verifyCode, Constants.sms_time);
		Validateinfo info = new Validateinfo();
		info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		info.setCheckCode(verifyCode);
		info.setAccount(mobile);
		memberService.inserValidateInfo(info);
		sess.setAttribute("r"+mobile, verifyCode);
		Response response = new Response();
		response.setData(verifyCode);
		return response;
	}
	
	/**
	 * 找回密码时手机发送的验证码
	 * @param req
	 * @throws IOException
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * @throws ApiException 
	 */
	@RequestMapping(value = "/rest/getSeekPwdMobileCode", method = RequestMethod.GET)
	public Response getSeekPwdMobileCode(HttpServletRequest req) throws IOException, ApiException {
		String mobile = req.getParameter("mobile");
		log.debug("获得手机验证码  mobile=="+mobile);
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
		// 生成随机字串
		String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
		log.debug("verifyCode == " + verifyCode);
		//发送验证码到手机
		//SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
		SDKSendTaoBaoSMS.sendFindPwdSMS(mobile, verifyCode, Constants.sms_time);
		Validateinfo info = new Validateinfo();
		info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
		info.setCheckCode(verifyCode);
		info.setAccount(mobile);
		memberService.inserValidateInfo(info);
		sess.setAttribute("s"+mobile, verifyCode);
		Response response = new Response();
		response.setData(verifyCode);

		return response;
	}

    /**
     * 会员注册
     * @param member
     * @return
     * @throws IOException 
     */
	@RequestMapping(value = "/rest/register", method = RequestMethod.POST)
	public Response register(Member member) throws Exception{
		log.debug("注册  mobile=="+member.getMobile()+" email =="+member.getEmail());
		Response result = new Response();
//		try {
			Subject currentUser = SecurityUtils.getSubject();
			Session sess = currentUser.getSession(false);
			//校验手机验证码是否正确
			if (member.getMobileCheckCode() != null) {
				String verifyCode = (String) sess.getAttribute("r" + member.getMobile());
				result = memberService.registerMobileMember(member, verifyCode);
			}
			if (member.getEmailCheckCode() != null) {
				String verifyCode = (String) sess.getAttribute("email");
				result = memberService.registerMailMember(member, verifyCode);
			}
//		}
/*		catch(BusinessException e)
		{
			result.setCode(400);
			result.setMsgCode(Integer.parseInt(e.getMsgid()));
			result.setMessage(e.getMessage());
		}*/
		return result;
	}
	
	 /**
     * 找回密码时填写账户名
     * @param member
     * @return
     * @throws IOException 
     */
	@RequestMapping(value = "/rest/writeAccount", method = RequestMethod.POST)
	public Response writeAccount(Member member) throws IOException {
		log.debug("seek pwd write account");
		Response result = new Response();
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
		String seekCode = (String) sess.getAttribute("seekPwdCode");
		log.info("writeAccount seekCode === "+seekCode);
		result = memberService.writeAccount(member, seekCode);

		return result;
	}
	
	/**
	 * 手机验证身份
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/mobileValidate", method = RequestMethod.POST)
	public Response mobileValidate(Member member) throws IOException {
		log.debug("找回密码  mobile =="+member.getMobile());
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
		String seekMobileCode = (String) sess.getAttribute("s"+member.getMobile());

		return memberService.mobileValidate(member, seekMobileCode);
	}
	
	/**
	 * 发送验证邮件密码重置
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/sendValidateMail", method = RequestMethod.POST)
	public Response sendValidateMail(Member member) throws IOException {
		log.debug("找回密码  email =="+member.getEmail());
		Response result = new Response();
		rvService.sendValidateMail(member, PropertiesUtils.getValue("host.ip"));
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

		return result;
	}
	
	/**
	 * 修改密码
	 * @param member
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/modifyPwd", method = RequestMethod.POST)
	public Response modifyPwd(Member member) throws IOException {
		log.debug("重置密码");
		Response response = new Response();
		int result = memberService.modifyPwd(member);
		Validateinfo info = new Validateinfo();
		info.setAccount(member.getAccount());
		memberService.deleteValidateInfo(info);

		return response;
	}
	
	/**
	 * 邮件激活账户
	 * @param req
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/rest/activateEmail", method = RequestMethod.GET)
	public ModelAndView activateEmail(HttpServletRequest req) throws UnsupportedEncodingException {
		log.debug("email activate start.....");
		Response response = new Response();
		ModelAndView modelAndView = new ModelAndView(); 
		try
        {
			String vm = req.getParameter("vm");//获取email
			if(vm != null & !vm.equals(""))
			{
				String decodeVM = new String (EncodeUtil.decodeBase64(vm));
	        	response = rvService.processActivate(decodeVM);
	        	modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(String.valueOf(response.getData()).getBytes()));
	        	RedirectView rv = new RedirectView(rvService.getRedirectUrl(response, "active"));
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
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/rest/validateMail", method = RequestMethod.GET)
	public ModelAndView validateMail(HttpServletRequest req) throws UnsupportedEncodingException {
		log.debug("validate mail start.....");
		String vm = req.getParameter("vm");//获取email
		ModelAndView modelAndView = new ModelAndView();  
		if(vm != null & !vm.equals(""))
		{
			Response response = new Response();
	        try
	        {
	        	response = rvService.processValidate(vm);
	        	String[] array = (String[]) response.getData();
	        	modelAndView.addObject("email", EncodeUtil.encodeBase64ToString(array[0].getBytes()));
	        	modelAndView.addObject("id",EncodeUtil.encodeBase64ToString(array[1].getBytes()));
	        	RedirectView rv = new RedirectView(rvService.getRedirectUrl(response,"validate"));
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
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/isValidatePass", method = RequestMethod.GET)
	public Response isValidatePass(HttpServletRequest req) throws IOException {
		log.debug("找回密码是否验证");
		Response response = new Response();
		String id = req.getParameter("account");
		int int_id = Integer.parseInt(EncodeUtil.decodeBase64ToString(id));
		int result = memberService.isValidatePass(int_id);
		if(result == 1)
		{
			Validateinfo vinfo = new Validateinfo();
	    	vinfo.setId(int_id);
		    vinfo.setValid(1);
		    memberService.updateValidateInfo(vinfo);
		}
		response.setData(result);

		return response;
	}
	
	/**
	 * 查看激活邮件
	 * @param req
	 * @throws IOException
	 */
	@RequestMapping(value = "/rest/watchMail", method = RequestMethod.GET)
	public Response watchMail(HttpServletRequest req) throws IOException {
		log.debug("找回密码是否验证");
		Response response = new Response();
		String account = req.getParameter("email");
		String result = ds.findMailAddress(EncodeUtil.decodeBase64ToString(account));
		response.setData(result);

		return response;
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
		genImgCode(response, verifyCode, out);
	}
}
