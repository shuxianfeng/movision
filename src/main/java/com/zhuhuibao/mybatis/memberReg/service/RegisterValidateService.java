package com.zhuhuibao.mybatis.memberReg.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.mapper.MemberRegMapper;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.SendEmail;

/**
 * 注册短信验证，邮箱验证业务类
 * @author penglong
 *
 */
@Service
public class RegisterValidateService {
	
	private static final Logger log = LoggerFactory.getLogger(RegisterValidateService.class);
	
	@Autowired
	private MemberRegService memberService;
	
	/**
	 * 发送邮件激活验证码
	 * @param member 会员信息
	 * @param serverIp 服务器IP
	 */
	public void sendMailActivateCode(Member member,String serverIp){

        ///邮件的内容
        StringBuffer sb=new StringBuffer("");
        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("筑慧宝账号激活：");
        sb.append("</strong>");
        sb.append("</p>");
        sb.append("<p style=\"margin:0; padding:20px 0 12px 0; color:#555555;\">您好！您于");
        sb.append(member.getRegisterTime());
        sb.append(" 注册了筑慧宝账号，请在24小时内点击以下链接，激活该账号：");
        sb.append("</p>");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"http://");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("\">http://");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm="); 
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString(),"筑慧宝-账号激活");
    }
	
	/**
	 * 邮件激活
	 * @param decodeVM  激活信息 id code
	 */
	public JsonResult processActivate(String decodeVM){  
        //数据访问层，通过email获取用户信息
		String[] array = decodeVM.split(",");
		int id = Integer.parseInt(array[0]);
		String email = array[1];
		JsonResult result = new JsonResult();
	    int code = 200;
        String message = "";
        List<Member> memberList =memberService.findMemberByMail(email);
        if(!memberList.isEmpty())
        {
	       for(Member user : memberList)
	       { 
	    	   if(user.getId() == id)
	    	   {
	    		 //验证用户激活状态  
		           if(user.getStatus()==0) { 
		               ///没激活
		               Date currentTime = new Date();//获取当前时间  
		               //验证链接是否过期 24小时
		               Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(user.getRegisterTime(),"yyyy-MM-dd HH:mm:ss"),5,1);
		               if(currentTime.before(registerDate)) {  
	                	   user.setStatus(1);
	                	   memberService.updateMemberStatus(user);
	                       message = "激活成功请登录";
	                       break;
		               } else { 
		            	   //激活码已过期
		            	   message = "激活码已过期";
		            	   code = 400;
		               }  
		           } else {
	        		   //邮箱已激活，请登录！
		        	   code = 400;
		        	   message = "邮箱已激活请登录";
		           }
	    	   }
	    	   else
	    	   {
	    		   //邮箱已被注册
	        	   code = 400;
	        	   message = "邮箱已被注册";
	    	   }
	       }
        }
        else {
    	   //该邮箱未注册（邮箱地址不存在）！
    	   code = 400;
    	   message = "该邮箱未注册";
	   }  
        result.setCode(code);
        result.setMessage(message);
        result.setData(email);
        return result;
       
   }
	
	/**
	 * 发送邮件重置密码
	 * @param member 会员信息
	 */
	public void sendValidateMail(Member member,String serverIp){

		StringBuffer sb=new StringBuffer("");
        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("亲爱的用户：");
        sb.append("</strong>");
        sb.append("<p>您好！");
        sb.append("</p>");
        sb.append("<p>您收到这封这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。</p>");
        sb.append("<p>请使用以下链接修改密码：</p>");
        sb.append("<p style=\"padding:0px");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"http://");
        sb.append(serverIp);
        sb.append("/rest/validateMail?action=validate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("\">http://");
        sb.append(serverIp);
        sb.append("/rest/validateMail?action=validate&vm="); 
        sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        sb.append("</p>为了保障您帐号的安全性，请在24小时内完成密码重置！</p>");
        sb.append("<div style=\"line-height:80px;height:80px\" </div>");
        sb.append("<p>筑慧宝团队</p>");
        sb.append("<p>");
        sb.append(DateUtils.date2Str(new Date(), "yyyy-MM-dd"));
        sb.append("</p>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString(),"筑慧宝-找回账户密码");
    }
	
	/**
	 * 密码重置时验证身份
	 * @param decodeVM
	 * @return
	 */
	public JsonResult processValidate(String email){  
        //数据访问层，通过email获取用户信息
        Member user = memberService.findMemberByAccount(email);
        JsonResult result = new JsonResult();
        int code = 200;
        String message = "";
        //验证用户是否存在 
        if(user!=null && user.getStatus() == 1) { 
        	int validateResult = memberService.isValidatePass(email);
        	if(validateResult == 0)
        	{
	            Date currentTime = new Date();//获取当前时间  
	            //验证链接是否过期 24小时
	            Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(user.getRegisterTime(),"yyyy-MM-dd HH:mm:ss"),5,1);
	            if(currentTime.before(registerDate)) {
	        	    user.setIsValidatePass(1);
	        	    memberService.updateMemberValidatePass(user);
	        	    message = "通过身份验证";
	            } else { 
	        	    //验证已过期
	        	    message = "验证身份已过期";
	        	    code = 400;
	            }  
        	}
        	else
        	{
        		message="密码找回链接已经失效";
        		code = 400;
        	}
       } else {
    	   //该邮箱未注册（邮箱地址不存在）！
    	   code = 400;
    	   message = "该邮箱未注册";
       }  
        result.setCode(code);
        result.setMessage(message);
        return result;
   }
	
	public static void main(String[] args) {
		Date currentTime = new Date();//获取当前时间  
		Date registerDate = DateUtils.date2Sub(DateUtils.str2Date("2016-02-02 11:13:30","yyyy-MM-dd HH:mm:ss"),5,1);
		System.out.println(currentTime.before(registerDate));
	}
}
