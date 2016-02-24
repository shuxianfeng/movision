package com.zhuhuibao.mybatis.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhuhuibao.mybatis.entity.JsonResult;
import com.zhuhuibao.mybatis.entity.member.Member;
import com.zhuhuibao.mybatis.mapper.MemberMapper;
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
	private MemberService memberService;
	
	/**
	 * 发送邮件激活验证码
	 * @param member 会员信息
	 */
	public void sendMailActivateCode(Member member){

        ///邮件的内容
        StringBuffer sb=new StringBuffer("点击下面链接激活账号，48小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");
        sb.append("<a href=\"http://192.168.1.100:8080/zhuhuibao/activateEmail?action=activate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("\">http://192.168.1.100:8080/zhuhuibao/activateEmail?action=activate&vm="); 
        sb.append(new String(EncodeUtil.encodeBase64(member.getId()+","+member.getEmail())));
        sb.append("</a>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString());
    }
	
	/**
	 * 邮件激活
	 * @param decodeVM  激活信息 id code
	 */
	public JsonResult processActivate(String decodeVM){  
        //数据访问层，通过email获取用户信息
		String[] array = decodeVM.split(",");
		String id = array[0];
		String email = array[1];
        Member user =memberService.findMemberById(id);
        JsonResult result = new JsonResult();
        int code = 200;
        String message = "";
        String data = id;
        //验证用户是否存在 
        if(user!=null) {  
           //验证用户激活状态  
           if(user.getStatus()==0) { 
               ///没激活
               Date currentTime = new Date();//获取当前时间  
               //验证链接是否过期 48小时
               Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(user.getRegisterTime(),"yyyy-MM-dd HH:mm:ss"),5,2);
               if(currentTime.before(registerDate)) {  
                   //验证激活码是否正确  
                   if(email.equals(user.getEmail())) {  
                       data = id;
                   } else {  
                	   //激活码不正确
                	   message = "激活码不正确";
                	   code = 400;
                   }  
               } else { 
            	   //激活码已过期
            	   message = "激活码已过期";
            	   code = 400;
               }  
           } else {
        	  //邮箱已激活，请登录！
        	   code = 200;
        	   message = "邮箱已激活请登录";
           }  
       } else {
    	   //该邮箱未注册（邮箱地址不存在）！
    	   code = 400;
    	   message = "该邮箱未注册";
       }  
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
   }
	
	/**
	 * 发送邮件重置密码
	 * @param member 会员信息
	 */
	public void sendValidateMail(Member member){

        ///邮件的内容
        StringBuffer sb=new StringBuffer("点击下面链接重置密码，48小时生效，否则重新发送邮件重置密码，链接只能使用一次，请尽快重置！</br>");
        sb.append("<a href=\"http://192.168.1.100:8080/zhuhuibao/validateMail?action=validate&vm=");
        sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("\">http://192.168.1.100:8080/zhuhuibao/validateMail?action=validate&vm="); 
        sb.append(new String(EncodeUtil.encodeBase64(member.getEmail())));
        sb.append("</a>");
        log.info("send email link == "+sb.toString());
        //发送邮件
        SendEmail.send(member.getEmail(), sb.toString());
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
        if(user!=null) {  
           //验证用户激活状态  
           if(user.getStatus()==0) { 
               ///没激活
               Date currentTime = new Date();//获取当前时间  
               //验证链接是否过期 48小时
               Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(user.getRegisterTime(),"yyyy-MM-dd HH:mm:ss"),5,2);
               if(currentTime.before(registerDate)) {  
                   //验证身份是否正确  
                   if(email.equals(user.getEmail())) {  
                   } else {  
                	   //验证不正确
                	   message = "验证身份不正确";
                	   code = 400;
                   }  
               } else { 
            	   //验证已过期
            	   message = "验证身份已过期";
            	   code = 400;
               }  
           } else {
        	  //验证身份通过请重置密码
        	   code = 200;
        	   message = " 验证身份通过请重置密码";
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
