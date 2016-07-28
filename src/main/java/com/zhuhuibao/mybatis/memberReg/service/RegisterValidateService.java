package com.zhuhuibao.mybatis.memberReg.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.SendEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 注册短信验证，邮箱验证业务类
 * @author penglong
 *
 */
@Service
public class RegisterValidateService {

	private static final Logger log = LoggerFactory.getLogger(RegisterValidateService.class);

	@Autowired
	MemberRegService memberService;

	/**
	 * 发送邮件激活验证码
     * @param id
     * @param serverIp 服务器IP
     */
	public void sendMailActivateCode(Long id, String email, String serverIp){

        ///邮件的内容
        StringBuilder sb=new StringBuilder("");
        sb.append("<div style=\"line-height:40px;height:40px\">");
        sb.append("</div>");
        sb.append("<p style=\"padding:0px\"");
        sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
        sb.append("筑慧宝账号激活：");
        sb.append("</strong>");
        sb.append("</p>");
        sb.append("<p style=\"margin:0; padding:20px 0 12px 0; color:#555555;\">您好！您已");
        sb.append(" 注册了筑慧宝账号，请在24小时内点击以下链接，激活该账号：");
        sb.append("</p>");
        sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(id+","+email)));
        sb.append("\">");
        sb.append(serverIp);
        sb.append("/rest/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(id+","+email)));
        sb.append("</a>");
        sb.append("</p>");
        sb.append("<p style=\"padding:0px;line-height:24px;font-size:12px;color:#979797;font-family:arial,sans-serif\">");
        sb.append("(如果您无法点击此链接，请将它复制到浏览器地址栏后访问)");
        sb.append("</p>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(email, sb.toString(),"筑慧宝-账号激活");
    }

	/**
	 * 邮件激活
	 * @param decodeVM  激活信息 id code
	 */
	public Response processActivate(String decodeVM){
        //数据访问层，通过email获取用户信息
		Response result = new Response();
		int code = 200;
	    int msgCode = MsgCodeConstant.mcode_common_success;
		try
		{
			String[] array = decodeVM.split(",");
			int id = Integer.parseInt(array[0]);
			String email = array[1];
	        String message = "";
	        List<Member> memberList =memberService.findMemberByMail(email);
	        if(!memberList.isEmpty())
	        {
	        	for(Member user : memberList)
	        	{
	        		//同一个邮箱多次注册
	        		if(user.getStatus() !=0)
	        		{
	        			if(id == user.getId())
	        			{
	        				code = 400;
				        	message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_actived));
				        	msgCode = MsgCodeConstant.member_mcode_mail_actived;
	        			}
	        			else
	        			{
	        				code = 400;
	 		        	    message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_registered));
	 		        	    msgCode = MsgCodeConstant.member_mcode_mail_registered;
	        			}
			        	result.setCode(code);
				        result.setMessage(message);
				        result.setData(email);
				        result.setMsgCode(msgCode);
				        return result;
	        		}
	        	}
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
		                       code = 200;
		                       break;
			               } else {
			            	   //激活码已过期
			            	   message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_active_code_expire));
			            	   code = 400;
			            	   msgCode = MsgCodeConstant.member_mcode_active_code_expire;

			               }
			           } else {
		        		   //邮箱已激活，请登录！
			        	   code = 400;
			        	   message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_actived));
			        	   msgCode = MsgCodeConstant.member_mcode_mail_actived;
			           }
		    	   }
		    	   else
		    	   {
		    		   //邮箱已被注册
		        	   code = 400;
		        	   message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_registered));
		        	   msgCode = MsgCodeConstant.member_mcode_mail_registered;
		    	   }
		       }
	        }
	        else {
	    	   //该邮箱未注册（邮箱地址不存在）！
	    	   code = 400;
	    	   message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_unregister));
	    	   msgCode = MsgCodeConstant.member_mcode_mail_unregister;
		   }
	        result.setCode(code);
	        result.setMessage(message);
	        result.setData(email);
	        result.setMsgCode(msgCode);
		}
		catch(Exception ex)
		{
			log.error("process activate mail error",ex);
			code = 400;
     	    msgCode = MsgCodeConstant.mcode_common_failure;
     	    result.setCode(code);
	        result.setMsgCode(msgCode);
	        return result;
		}
        return result;

   }

	/**
	 * 发送邮件重置密码
	 * @param mail 邮箱地址
	 */
	public void sendValidateMail(String mail,String serverIp){
		log.info("send validate mail = "+mail);
		Validateinfo vInfo = new Validateinfo();
		vInfo.setAccount(mail);
		vInfo = memberService.findMemberValidateInfo(vInfo);
		if(vInfo != null && vInfo.getId() != null && vInfo.getValid() == 0) {
			String currentTime = DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss");
			String url = serverIp + "/rest/validateMail?vm=" + new String(EncodeUtil.encodeBase64("validate," + mail + "," + currentTime));
			StringBuilder sb = new StringBuilder("");
			sb.append("<div style=\"line-height:40px;height:40px\">");
			sb.append("</div>");
			sb.append("<p style=\"padding:0px\"");
			sb.append("<strong style=\"font-size:14px;line-height:24px;color:#333333;font-family:arial,sans-serif\"");
			sb.append("亲爱的用户：");
			sb.append("</strong>");
			sb.append("<p>您好！");
			sb.append("</p>");
			sb.append("<p>您收到这封电子邮件是因为您 (也可能是某人冒充您的名义) 申请了一个新的密码。假如这不是您本人所申请, 请不用理会这封电子邮件, 但是如果您持续收到这类的信件骚扰, 请您尽快联络管理员。</p>");
			sb.append("<p>请使用以下链接修改密码：</p>");
			sb.append("<p style=\"padding:0px\">");
			sb.append("<a style=\"line-height:24px;font-size:12px;font-family:arial,sans-serif;color:#0000cc\" href=\"");
			sb.append(url);
			sb.append("\">");
			sb.append(url);
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
			log.info("send email link == " + sb.toString());
			//发送邮件
			SendEmail.send(mail, sb.toString(), "筑慧宝-找回账户密码");
			vInfo.setCheckCode(url);
			memberService.updateValidateInfo(vInfo);
		}else {
			throw new BusinessException(MsgCodeConstant.MEMBER_SEED_PWD_ERROR,"找回密码错误");
		}
    }

	/**
	 * 密码重置时验证身份
	 * @param validateInfo 用户验证信息
	 * @return
	 */
	public Response processValidate(String validateInfo){
		String url = PropertiesUtils.getValue("host.ip") + "/rest/validateMail?vm=" + validateInfo;
		//数据访问层，通过email获取用户信息
		String decodeInfo = new String(EncodeUtil.decodeBase64(validateInfo));
		String[] arr = decodeInfo.split(",");
		String email = arr[1];
		Member user = memberService.findMemberByAccount(email);
		Response result = new Response();
		int code = 200;
		int msgCode = MsgCodeConstant.mcode_common_success;
		String id = "0";
		String message;
		//验证用户是否存在
		if (user != null && (user.getStatus() != 0 || user.getStatus() != 2)) {
			Validateinfo vinfo = new Validateinfo();
			vinfo.setCheckCode(url);
			vinfo = memberService.findMemberValidateInfo(vinfo);
			if (vinfo != null && vinfo.getValid() == 0 && vinfo.getCreateTime() != null) {
				Date currentTime = new Date();//获取当前时间
				//验证链接是否过期 24小时
				Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(vinfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 5, 1);
				if (currentTime.before(registerDate)) {
					message = "通过身份验证";
					id = String.valueOf(vinfo.getId());
				} else {
					//验证已过期
					message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_validate_expire));
					code = 400;
					msgCode = MsgCodeConstant.member_mcode_mail_validate_expire;
				}
			} else {
				memberService.deleteValidateInfo(vinfo);
				message= MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_url_invalid));
				code = 400;
				msgCode = MsgCodeConstant.member_mcode_mail_url_invalid;
			}
		} else {
			//该邮箱未注册（邮箱地址不存在）！
			code = 400;
			message = MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_unregister));
			msgCode = MsgCodeConstant.member_mcode_mail_unregister;
		}
		result.setCode(code);
		result.setMessage(message);
		String[] str = {email, id};
		result.setData(str);
		result.setMsgCode(msgCode);
		return result;

   }

	/**
	 * 获得跳转页面的URL
	 * @param response
	 * @return
	 */
    public String getRedirectUrl(Response response, String type) {
		String redirectUrl;
		if(type.equals("validate"))
		{
			if(response.getCode() == 200)
			{
				redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("reset.pwd.page");
			}
			else
			{
				if(MsgCodeConstant.member_mcode_mail_validate_expire == response.getMsgCode())
				{
					//验证邮件过期
					redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email.validate.expire.page");
				}
				else
				{
					//多次点击链接失效
					redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("reset.pwd.invalid.page");
				}
			}
		}
		else
		{
			if(response.getCode() == 200)
	    	{
	    		//跳转到会员中心页面
	    		redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("active.mail.page");
	    	}
	    	else
	    	{
	    		if(MsgCodeConstant.member_mcode_active_code_expire == response.getMsgCode())
	    		{
	    			//激活邮件超过24小时
	    			redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email.active.expire.page");
	    		}
	    		else
	    		{
	    			//点击重复激活
	    			redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("active.mail.replay.page");
	    		}
	    	}
		}
		return redirectUrl;
	}

}
