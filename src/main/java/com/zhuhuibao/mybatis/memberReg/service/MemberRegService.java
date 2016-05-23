package com.zhuhuibao.mybatis.memberReg.service;

import java.util.*;

import com.zhuhuibao.utils.exception.BusinessException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memberReg.entity.Member;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.mapper.MemberRegMapper;
import com.zhuhuibao.mybatis.memberReg.mapper.ValidateinfoMapper;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.shiro.realm.ShiroRealm.ShiroUser;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ResourcePropertiesUtils;

/**
 * 会员业务处理
 * @author penglong
 *
 */
@Service
@Transactional
public class MemberRegService {
	private static final Logger log = LoggerFactory.getLogger(MemberRegService.class);
	
	@Autowired
	private MemberRegMapper memberRegMapper;
	
	@Autowired
	private ValidateinfoMapper viMapper;
	
	@Autowired
	private DictionaryService ds;
	
	@Autowired
	MemberRegMapper memberMapper;
	
	/**
	 * 注册验证码业务类
	 */
	@Autowired
	private RegisterValidateService rvService;
	
	
	/**
     * 注册用户
     */
    public int registerMember(Member member)
    {
    	log.debug("注册会员");
    	int memberId = 0;
    	try
    	{
	    	if(member != null)
	    	{
	    		//前台传过来的base64密码解密
	    		String pwd = new String(EncodeUtil.decodeBase64(member.getPassword()));
	    		String md5Pwd = new Md5Hash(pwd,null,2).toString();
				member.setPassword(md5Pwd);
	    		if(member.getMobile() != null)
	    		{
	    			//默认状态为“0”
	    			member.setStatus(1);
	    		}
	    		member.setRegisterTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
	    		//注册成功的默认为“管理员”类型
	    		member.setWorkType(100);
	    		memberId = memberRegMapper.registerMember(member);
	    	}
    	}
    	catch(Exception e)
    	{
    		log.error("register member error",e);
    	}
    	return memberId;
    }
    
    /**
     * 根据账户名认证会员
     * @param memberAccount 会员账号
     * @return
     */
    public Member findMemberByAccount(String memberAccount)
    {
    	log.info("findMemberByAccount accunt =="+memberAccount);
    	Member member = new Member();
    	try
    	{
	    	if(memberAccount != null && memberAccount.indexOf("@") >= 0)
	    	{
	    		member.setEmail(memberAccount);
	    	}
	    	else
	    	{
	    		member.setMobile(memberAccount);
	    	}
	    	member = memberRegMapper.findMemberByAccount(member);
    	}
    	catch(Exception e)
    	{
    		log.error("find memeber by account error",e);
    	}
    	return member;
    }


    
    /**
     * 根据邮箱查询会员信息
     * @param email 邮箱地址
     * @return
     */
    public List<Member> findMemberByMail(String email)
    {
    	log.debug("find memberinfo by email = "+email);
    	List<Member> memberList = new ArrayList<Member>();
    	try
    	{
    		memberList = memberRegMapper.findMemberByMail(email);
    	}
    	catch(Exception e)
    	{
    		log.error("find memeber list by mail ");
    	}
    	return memberList;
    }
    
    /**
     * 找回密码是否验证通过
     * @param id 会员id
     * @return
     */
    public int isValidatePass(int id)
    {
    	log.debug("find password validate account = "+id);
    	int result = 0;
    	try
    	{
    		Validateinfo info = new Validateinfo();
    		info.setId(id == 0 ? -1 : id);
    		info = this.findMemberValidateInfo(info);
	    	if(info != null && info.getValid() == 0)
	    	{
	    		result = 1;
	    	}
    	}
    	catch(Exception e)
    	{
    		log.error("is validate pass error",e);
    	}
    	return result;
    }
    
    /**
     * 注册的账户名是否存在 
     * @param member 会员信息
     * @return result 0:不存在，1:存在
     */
    public int isExistAccount(Member member)
    {
    	int result = 0;
    	try
    	{
    		result = memberRegMapper.isExistAccount(member);
    	}
    	catch(Exception e)
    	{
    		log.error("is exist account ",e);
    	}
    	return result;
    }
    
    public int updateMemberStatus(Member member)
    {
    	int result = 0;
    	try
    	{
    		result = memberRegMapper.updateMemberStatus(member);
    	}
    	catch(Exception e)
    	{
    		log.error("update member status",e);
    	}
    	return result;
    }
    
    /**
     * 更新找回密码邮箱验证是否通过
     * @param member
     * @return
     */
    public int updateMemberValidatePass(Member member)
    {
    	int result = 0;
    	try
    	{
    		result = memberRegMapper.updateMemberValidatePass(member);
    	}
    	catch(Exception e)
    	{
    		log.error("update member validatePass error",e);
    	}
    	return result;
    }
    
    /**
     * 找回密码，重置密码
     * @param member 会员信息
     * @return
     */
    public int modifyPwd(Member member)
    {
    	log.info("modify account password account = "+member.getAccount());
    	int result = 0;
    	try
    	{
	    	String pwd = new String(EncodeUtil.decodeBase64(member.getPassword()));
			String md5Pwd = new Md5Hash(pwd,null,2).toString();
			member.setPassword(md5Pwd);
			if(member.getAccount() != null)
			{
				if(member.getAccount().indexOf("@") >= 0)
				{
					member.setEmail(member.getAccount());
					result = memberRegMapper.updateMemberPwd(member);
				}
				else
				{
					member.setMobile(member.getAccount());
					result = memberRegMapper.updateMemberPwd(member);
				}
				Validateinfo info = new Validateinfo();
				info.setAccount(member.getAccount());
				this.deleteValidateInfo(info);
			}
    	}
    	catch(Exception e)
    	{
    		log.error("modify password error!",e);
    	}
    	return result;
    }
    
    /**
     * 填写账户名信息
     * @param member 会员信息
     * @param seekPwdCode 查找密码的验证code
     * @return
     */
    public JsonResult writeAccount(Member member,String seekPwdCode)
    {
    	JsonResult result = new JsonResult();
    	try
    	{
	    	String data = "";
	    	//手机
	    	if(member.getAccount() != null && member.getCheckCode() != null)
			{
	    		if(member.getAccount().indexOf("@") == -1)
	    		{
		    		data = member.getMobile();
					if(member.getCheckCode().equalsIgnoreCase(seekPwdCode))
					{
						member.setMobile(member.getAccount());
						Member dbmember = memberRegMapper.findMemberByAccount(member);
				    	if(dbmember == null)
				    	{
				    		result.setCode(400);
				    		result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
				    		result.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
				    	}
					}
					else
					{
						result.setCode(400);
						result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
						result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
					}
	    		}
	    		else
	    		{
	    			data = member.getEmail();
	    			if(member.getCheckCode().equalsIgnoreCase(seekPwdCode))
	    			{
	    				member.setEmail(member.getAccount());
	    				Member dbmember = memberRegMapper.findMemberByAccount(member);
	    		    	if(dbmember == null)
	    		    	{
	    		    		result.setCode(400);
	    		    		result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
	    		    		result.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
	    		    	}
	    		    	else
	    		    	{
	    		    		member.setEmailCheckCode(member.getCheckCode());
	    		    		memberRegMapper.updateEmailCode(member);
	    		    	}
	    			}
	    			else
	    			{
	    				result.setCode(400);
	    				result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_validate_error)));
	    				result.setMsgCode(MsgCodeConstant.member_mcode_mail_validate_error);
	    			}
	    		}
			}
			result.setData(data);
    	}
    	catch(Exception e)
    	{
    		log.error("write account error!");
    	}
    	return result;
    }
    
    /**
     * 插入会员验证信息
     * @param info
     * @return
     */
    public JsonResult inserValidateInfo(Validateinfo info)
    {
    	JsonResult result = new JsonResult();
    	try
    	{
    		viMapper.insertValidateInfo(info);
    	}
    	catch(Exception e)
    	{
    		log.error("insert validate info error",e);
    	}
    	return result;
    }
    
    /**
     * 查找会员验证相关的信息
     * @param info
     * @return
     */
    public Validateinfo findMemberValidateInfo(Validateinfo info)
    {
    	Validateinfo vinfo = new Validateinfo();
    	try
    	{
    		vinfo = viMapper.findMemberValidateInfo(info);
    	}
    	catch(Exception e)
    	{
    		log.error("find member validate info",e);
    	}
    	return vinfo;
    }
    
    /**
     * 更新会员验证信息
     * @param info
     * @return
     */
    public int updateValidateInfo(Validateinfo info)
    {
    	int result = 0;
    	try
    	{
    		result = viMapper.updateValidateInfo(info);
    	}
    	catch(Exception e)
    	{
    		log.error("insert validate info error",e);
    	}
    	return result;
    }
    
    /**
     * 删除会员验证信息
     * @param info
     * @return
     */
    public int deleteValidateInfo(Validateinfo info)
    {
    	int result = 0;
    	try
    	{
    		result = viMapper.deleteValidateInfo(info);
    	}
    	catch(Exception e)
    	{
    		log.error("insert validate info error",e);
    	}
    	return result;
    }
    
    /**
     * 邮件验证
     * @param member  会员信息
     * @param seekMobileCode 图形验证码
     * @return
     */
    public JsonResult mobileValidate(Member member, String seekMobileCode) {
		JsonResult result = new JsonResult();
		if(seekMobileCode != null)
		{
			Validateinfo info = new Validateinfo();
			info.setAccount(member.getMobile());
			info.setValid(0);
			info.setCheckCode(seekMobileCode);
			info = this.findMemberValidateInfo(info);
			Date currentTime = new Date();
			Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"),12,10);
			if(currentTime.before(sendSMStime)) 
			{
				if(info != null)
				{
					if(seekMobileCode == null || member.getMobileCheckCode() == null || !member.getMobileCheckCode().equals(info.getCheckCode()))
					{
						result.setCode(400);
						result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
						result.setData(member.getMobile());
						result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
					}
				}
			}
			else
			{
				this.deleteValidateInfo(info);
				result.setCode(400);
				result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
				result.setMsgCode(MsgCodeConstant.member_mcode_sms_timeout);
			}
		}
		else
		{
			result.setCode(400);
			result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
			result.setData(member.getMobile());
			result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
		}
		return result;
	}
    
    /**
     * 注册手机会员
     * @param member
     * @param verifyCode
     * @return
     */
    public JsonResult registerMobileMember(Member member,String verifyCode) {
		JsonResult result = new JsonResult();
		if(verifyCode != null)
		{
			Validateinfo info = new Validateinfo();
			info.setAccount(member.getMobile());
			info.setValid(0);
			info.setCheckCode(verifyCode);
			info = this.findMemberValidateInfo(info);
			Date currentTime = new Date();
			Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"),12,10);
			if(currentTime.before(sendSMStime)) 
			{ 
				if(info != null &&  info.getCheckCode().equalsIgnoreCase(member.getMobileCheckCode()))
				{
					int isExist = this.isExistAccount(member);
					if(isExist == 0)
					{
						this.registerMember(member);
						this.deleteValidateInfo(info);
						
						ShiroUser shrioUser = new ShiroUser(member.getId(), member.getMobile(), member.getStatus());
						Subject currentUser = SecurityUtils.getSubject();
						Session session = currentUser.getSession();
						session.setAttribute("member", shrioUser);
					}
					else
					{
						result.setCode(400);
						result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
						result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);
					}
				}
				else
				{
					result.setCode(400);
					result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
					result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
				}
				log.debug("mobile verifyCode == " + member.getMobileCheckCode());
			}
			else
			{
				this.deleteValidateInfo(info);
				result.setCode(400);
				result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
				result.setMsgCode(MsgCodeConstant.member_mcode_sms_timeout);
			}
		}
		else
		{
			result.setCode(400);
			result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
			result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
		}
		return result;
	}
    
    /**
     * 邮件账号会员注册
     * @param member
     * @param verifyCode
     * @return
     */
    public JsonResult registerMailMember(Member member, String verifyCode) throws BusinessException{
    	JsonResult result = new JsonResult();
		if(verifyCode != null && verifyCode.equalsIgnoreCase(member.getEmailCheckCode()) )			{
			if(member.getEmail().indexOf("@")>=0)
			{
				int isExist = this.isExistAccount(member);
				if(isExist == 0)
				{
					this.registerMember(member);
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
				/*	result.setCode(400);
					result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
					result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);*/
					throw new BusinessException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
				}
			}
		}
		else
		{
			result.setCode(400);
			result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_validate_error)));
			result.setMsgCode(MsgCodeConstant.member_mcode_mail_validate_error);
		}
		log.debug("email verifyCode == " + member.getEmailCheckCode());
		return result;
	}
    
    /**
	 * 会员信息
	 * @param id
	 * @return
	 */
	public JsonResult findMemberInfoById(Long id)
	{
		JsonResult jsonResult = new JsonResult();
		try
		{
			Member member = memberMapper.findMemberInfoById(id);
			jsonResult.setData(member);
		}
		catch(Exception e)
		{
			log.error("find member info error!",e);
			jsonResult.setCode(MsgCodeConstant.response_status_400);
    		jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
    		jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
    		return jsonResult;
		}
		return jsonResult;
	}
}
