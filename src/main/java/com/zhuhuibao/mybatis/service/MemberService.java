package com.zhuhuibao.mybatis.service;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.entity.JsonResult;
import com.zhuhuibao.mybatis.entity.member.Member;
import com.zhuhuibao.mybatis.mapper.MemberMapper;
import com.zhuhuibao.security.EncodeUtil;

/**
 * 会员业务处理
 * @author penglong
 *
 */
@Service
@Transactional
public class MemberService {
	private static final Logger log = LoggerFactory.getLogger(MemberService.class);
	
	@Autowired
	private MemberMapper memberMapper;
	
	/**
     * 注册用户
     */
    public int registerMember(Member member)
    {
    	log.debug("注册会员");
    	int memberId = 0;
    	if(member != null && member.getIdentify() != 0)
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
    		memberId = memberMapper.registerMember(member);
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
    	Member member = new Member();
    	if(memberAccount != null && memberAccount.indexOf("@") > 0)
    	{
    		member.setEmail(memberAccount);
    	}
    	else
    	{
    		member.setMobile(memberAccount);
    	}
    	return memberMapper.findMemberByAccount(member);
    }
    
    /**
     * 根据ID查询会员信息
     * @param memberId 会员id
     * @return
     */
    public Member findMemberById(String memberId)
    {
    	log.debug("find memberinfo by memberId = "+memberId);
    	return memberMapper.findMemberById(memberId);
    }
    
    /**
     * 找回密码是否验证通过
     * @param memberId 会员id
     * @return
     */
    public int isValidatePass(String account)
    {
    	log.debug("find password validate account = "+account);
    	Integer obj = memberMapper.isValidatePass(account);
    	return 1;
    }
    
    /**
     * 完善会员基本信息
     * @param member
     * @return
     */
    public int addMemberBaseInfo(Member member)
    {
    	log.info("add member base info memeberId = "+member.getId());
    	return memberMapper.addMemberBaseInfo(member);
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
    	String pwd = new String(EncodeUtil.decodeBase64(member.getPassword()));
		String md5Pwd = new Md5Hash(pwd,null,2).toString();
		member.setPassword(md5Pwd);
		if(member.getAccount() != null)
		{
			if(member.getAccount().indexOf("@") > 0)
			{
				member.setEmail(member.getAccount());
				result = memberMapper.updateMemberPwdByMail(member);
			}
			else
			{
				member.setMobile(member.getAccount());
				result = memberMapper.updateMemberPwdByMobile(member);
			}
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
    	String data = "";
    	//手机
    	if(member.getAccount() != null && member.getCheckCode() != null)
		{
    		if(member.getAccount().indexOf("@") == -1)
    		{
	    		data = member.getMobile();
				if(member.getCheckCode().equals(seekPwdCode))
				{
					member.setMobile(member.getAccount());
					Member dbmember = memberMapper.findMemberByAccount(member);
			    	if(dbmember == null)
			    	{
			    		result.setCode(400);
			    		result.setMessage("账户不存在");
			    	}
				}
				else
				{
					result.setCode(400);
					result.setMessage("验证码错误");
				}
    		}
    		else
    		{
    			data = member.getEmail();
    			if(member.getMobileCheckCode().equals(seekPwdCode))
    			{
    				member.setEmail(member.getAccount());
    				Member dbmember = memberMapper.findMemberByAccount(member);
    		    	if(dbmember == null)
    		    	{
    		    		result.setCode(400);
    		    		result.setMessage("账户不存在");
    		    	}
    			}
    			else
    			{
    				result.setCode(400);
    				result.setMessage("验证码错误");
    			}
    		}
		}
		result.setData(data);
    	return result;
    }
}
