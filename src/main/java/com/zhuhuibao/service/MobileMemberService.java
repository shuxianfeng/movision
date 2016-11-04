package com.zhuhuibao.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.service.impl.MembersService;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.Message;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.sms.SDKSendSms;

/**
 * 会员中心业务处理实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileMemberService {

    private static final Logger log = LoggerFactory.getLogger(MobileMemberService.class);

    public static final String SUCCESS = "SUCCESS";
    
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberService oldMemberService;

    @Autowired
    private MembersService membersService;

    @Autowired
    private MemInfoCheckService memInfoCheckService;

    @Autowired
    private MemberRegService memberRegService;
    
    
    /**
     * 获取当前用户绑定的手机号
     * @return
     */
    public Response getCurUserMobile(){
    	Response res = new Response();
    	Long memberId = ShiroUtil.getCreateID();
    	String mobile = getMobileByMemberId(memberId);
    	Map map = new HashMap<>();
    	if(org.apache.commons.lang3.StringUtils.isEmpty(mobile)){
    		map.put("IS_EXIST_MOBILE", "NO");
        	map.put("MOBILE", "");
    	}else{
    		map.put("IS_EXIST_MOBILE", "YES");
    		String encode_mobile = mobile.substring(0,3) + "******" + mobile.substring(9, mobile.length());
        	map.put("MOBILE", encode_mobile);
    	}
    	res.setData(map);
    	return res;
    }
    
    /**
     * 生成图片验证码
     * @param response
     * @throws IOException
     */
    public void getImgCode(HttpServletResponse response) throws IOException{
    	Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100, 40, response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute("first_bind_mobile_imgVerifyCode", verifyCode);
    }
    
    
    /**
     * 校验旧手机号的短信验证码
     * @param code
     * @return
     */
    public Response chkOldMobile(String code){
    	Response result = new Response();
        
    	Long memberId = ShiroUtil.getCreateID();
    	String mobile = getMobileByMemberId(memberId);
    	
    	String sessionCode = getSessionCode(mobile);
    	
    	validationParams(mobile, code, memberId);
    	
        Validateinfo info = getValidationInfo(mobile, code);
        if(null == info){
        	//手机验证码不正确
        	smsException(mobile, code, result);
        	return result;
		}
		Date currentTime = new Date();	//当前时间
		//短信验证码有效期十分钟
		Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 12, 10);
		
		if (currentTime.before(sendSMStime)) {
			if(sessionCode.equals(code)){
                //前端：进入到绑定新的手机号页面。
				result.setData(genSuccessReturnMap(SUCCESS, "校验手机短信验证码成功！"));
	        }else{
	        	//手机验证码不正确
	        	smsException(mobile, code, result);
	        }
		}else{
			//验证码超时
			smsTimeOutException(result, info);
		}
        return result;
    }

    /**
     * 通过会员id获取会员绑定的手机号
     * @param memberId
     * @return
     */
	private String getMobileByMemberId(Long memberId) {
    	Member member = oldMemberService.findMemById(String.valueOf(memberId));
    	if(null == member){
    		throw new BusinessException(MsgCodeConstant.NOT_EXIST_MEMBER, "不存在该会员信息");
    	}
    	String mobile = member.getMobile();
		return mobile;
	}

    /**
     * 获取session中的短信验证码
     * @param mobile
     * @return
     */
	private String getSessionCode(String mobile) {
		Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessionCode = (String) sess.getAttribute("mobile_verifycode_"+mobile);
		return sessionCode;
	}
    
    /**
     * 绑定手机号
     * @param mobile	
     * @param code	短信验证码
     */
    public Response bindMobile(String mobile, String code){
    	Response result = new Response();
    	
    	Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessionCode = (String) sess.getAttribute("mobile_verifycode_"+mobile);
        
        Member member = new Member();
    	Long memberId = ShiroUtil.getCreateID();
    	
    	validationParams(mobile, code, memberId);
    	
        Validateinfo info = getValidationInfo(mobile, code);
        if(null == info){
        	//手机验证码不正确
        	smsException(mobile, code, result);
        	return result;
		}
		Date currentTime = new Date();	//当前时间
		//短信验证码有效期十分钟
		Date sendSMStime = DateUtils.date2Sub(DateUtils.str2Date(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"), 12, 10);
		
		if (currentTime.before(sendSMStime)) {
			if(sessionCode.equals(code)){
				
                updateMemberMobile(mobile, member, memberId);
//                ShiroUser m = (ShiroUser) sess.getAttribute("member");
//                m.setAccount(account);
                result.setData(genSuccessReturnMap(SUCCESS, "绑定手机号成功！"));
	        }else{
	        	//手机验证码不正确
	        	smsException(mobile, mobile, result);
	        }
		}else{
			//验证码超时
			smsTimeOutException(result, info);
		}
        return result;
    }
    /**
     * 修改会员信息中的手机号
     * @param mobile
     * @param member
     * @param memberId
     */
	private void updateMemberMobile(String mobile, Member member, Long memberId) {
		member.setId(String.valueOf(memberId));
		member.setMobile(mobile);
		oldMemberService.updateMemInfo(member);
	}
    
    /**
     * 验证码超时
     * @param result
     * @param info
     */
	private void smsTimeOutException(Response result, Validateinfo info) {
		memberRegService.deleteValidateInfo(info);
		result.setCode(400);
		result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_sms_timeout)));
		result.setMsgCode(MsgCodeConstant.member_mcode_sms_timeout);
	}
	/**
	 * 验证码不正确
	 * @param mobile
	 * @param result
	 */
	private void smsException(String mobile, String code, Response result) {
		result.setCode(400);
		result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
		Map m = new HashMap<>();
		m.put("MOBILE", mobile);
		m.put("SMS_CODE", code);
		
		result.setData(m);
		result.setMsgCode(MsgCodeConstant.member_mcode_mobile_validate_error);
	}

    /**
     * 参数校验
     * @param mobile
     * @param code
     * @param memberId
     */
	private void validationParams(String mobile, String code, Long memberId) {
		if(null == memberId){
    		//抛出未登陆异常
    		throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
    	}
    	if(StringUtils.isEmpty(mobile)){
    		throw new BusinessException(MsgCodeConstant.MOBILE_IS_EMPTY, "手机号是空");
    	}
    	if(StringUtils.isEmpty(code)){
    		throw new BusinessException(MsgCodeConstant.SMS_VERIFY_CODE_IS_EMPTY, "手机短信验证码是空");
    	}
	}

	/**
	 * 获取手机验证信息
	 * @param mobile
	 * @param code
	 * @return
	 */
	private Validateinfo getValidationInfo(String mobile, String code) {
		Validateinfo info = new Validateinfo();
		info.setAccount(mobile);	//会员账号
		info.setValid(0);	//有效
		info.setCheckCode(code);	//验证码
		info = memberRegService.findMemberValidateInfo(info);
		
		return info;
	}
    
	/**
	 * 获取手机短信验证码
	 * @return
	 */
	public Response getSMSVerifyCode(){
		Long memberid = ShiroUtil.getCreateID();
		Member member = oldMemberService.findMemById(String.valueOf(memberid));
		String mobile = member.getMobile();
		
		Response res = sendMobileVerifyCode(mobile);
		return res;
		
	}
	
	
 
    /**
     * 生成手机验证码并发送
     * @param mobile
     */
    public Response sendMobileVerifyCode(String mobile){
    	
    	Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
    	
    	// 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4, VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        //发送验证码到手机
        String json = convertVerifyMap2Json(verifyCode);
        SDKSendSms.sendSMS(mobile, json, PropertiesUtils.getValue("modify_mobile_sms_template_code"));
        addValidationInfo(mobile, verifyCode);
        //把生成的手机验证码缓存到session
        sess.setAttribute("mobile_verifycode_" + mobile, verifyCode);
        
        Response result = new Response();
        result.setData(genSuccessReturnMap(SUCCESS, "手机短信验证码发送成功！"));
        return result;
    }

    /**
     * 把验证码的map转换成json格式
     * @param verifyCode
     * @return
     */
	private String convertVerifyMap2Json(String verifyCode) {
		Map<String, String> map = new LinkedHashMap<>();
        map.put("code", verifyCode);
        map.put("time", Constants.sms_time);
        Gson gson = new Gson();
        String json = gson.toJson(map);
		return json;
	}


    /**
     * 增加一条验证信息
     * @param mobile
     * @param verifyCode
     */
	private void addValidationInfo(String mobile, String verifyCode) {
		Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);
        memberRegService.inserValidateInfo(info);
	}
    
    /**
     * 校验图形验证码
     * @param imgCode
     * @return
     */
    public boolean verifyImgCode(String imgCode){
    	boolean flag = true;
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessionCode = (String) sess.getAttribute("first_bind_mobile_imgVerifyCode");
        
        if(!(sessionCode.toLowerCase()).equals((imgCode.toLowerCase()))){
        	flag = false;
        }
    	return flag;
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
	public void getImageVerifyCode(HttpServletRequest req,
			HttpServletResponse response,int imgWidth,int imgheight,int verifySize,String key) {
		log.debug("获取验证码，开始");
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
			//输出指定验证码图片流 
			VerifyCodeUtils.outputImage1(imgWidth, imgheight, out, verifyCode);
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
		log.debug("获取验证码，结束");
	}
    
    /**
     * 校验手机号码
     * @param mobile
     * @return
     */
    public Response validateMobile(String mobile){
    	Response result = new Response();
    	
    	if(!ValidateUtils.isMobile(mobile)){
    		mobilePatternException(mobile, result);
    		return result;
    	}
    	//判断拥有该手机号的会员是否存在
    	Member member = new Member();
        member.setMobile(mobile);
        Member member1 = oldMemberService.findMember(member);
        if (member1 != null) {
        	accountWithMobileExistException(mobile, result);
        	return result;
        }
        
        result.setData(genSuccessReturnMap(SUCCESS, "手机号校验成功！"));
        return result;
    }
    
    /**
     * 根据新手机号获取图形验证码
     * @param response
     * @param mobile
     * @return
     * @throws IOException
     */
    public Response genImgCodeByNewMobile(HttpServletResponse response ,String mobile) throws IOException{
    	Response result = new Response();
    	
    	if(!ValidateUtils.isMobile(mobile)){
    		mobilePatternException(mobile, result);
    		return result;
    	}
    	//判断拥有该手机号的会员是否存在
    	Member member = new Member();
        member.setMobile(mobile);
        Member member1 = oldMemberService.findMember(member);
        if (member1 != null) {
        	accountWithMobileExistException(mobile, result);
        	return result;
        }
        
        getImgCode(response);
        result.setData(genSuccessReturnMap(SUCCESS, "手机号校验成功，并且图片验证码生成成功"));
        
        return result;
    	
    	
    }
    

    /**
     * 生成成功返回结果的map
     * @param status
     * @param msg
     * @return
     */
	private Map genSuccessReturnMap(String status, String msg) {
		Map map = new HashMap<>();
        map.put("STATUS", status);
        map.put("MSG", msg);
		return map;
	}
    /**
     * 您的输入的手机号已与其他账号绑定
     * @param mobile
     * @param result
     */
	private void accountWithMobileExistException(String mobile, Response result) {
		result.setCode(400);
		result.setMessage("您的输入的手机号已与其他账号绑定");
		result.setData(mobile);
		result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);
	}
    /**
     * 手机格式异常
     * @param mobile
     * @param result
     */
	private void mobilePatternException(String mobile, Response result) {
		result.setCode(400);
		result.setMessage("手机格式不正确");
		result.setData(mobile);
		result.setMsgCode(MsgCodeConstant.MOBILE_PATTERN_ERROR);
		
	}
    
    /**
     * 获取名企列表
     *
     * @return list
     */
    public Paging<Member> getGreatCompany(Paging<Member> pager, String identify) throws Exception {
        List<Member> members = memberMapper.findGreatCompanyByPager(pager.getRowBounds(), identify);
        pager.result(members);
        return pager;
    }

    /**
     * 获取名企详情信息
     *
     * @return list
     */
    public Map getGreatCompanyInfo(String id) throws Exception {
        return oldMemberService.introduce(id, "2");
    }

    /**
     * 搜索工程商
     *
     * @param spec
     *            查询条件
     * @return map
     * @throws ServiceException
     */
    public Map<String, Object> searchContractors(ContractorSearchSpec spec) throws ServiceException {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        return membersService.searchContractors(spec);
    }

    /**
     * 查询memberChk信息
     *
     * @param id
     * @return
     */
    public MemInfoCheck getMemInfoCheckById(Long id) {
        try {
            return memInfoCheckService.findMemById(String.valueOf(id));
        } catch (Exception e) {
            log.error("执行异常>>", e);
            throw e;
        }
    }

    /**
     * 更新会员审核信息
     *
     * @param memInfoCheck
     */
    public void updateMemberInfoCheck(MemInfoCheck memInfoCheck) {
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        // 保证接口更新当前登录人数据
        memInfoCheck.setId(loginMember.getId());
        // 设置修改后的数据状态
        if (loginMember.getStatus() != MemberConstant.MemberStatus.WJH.intValue() || loginMember.getStatus() != MemberConstant.MemberStatus.ZX.intValue()) {
            memInfoCheck.setStatus(MemberConstant.MemberStatus.WSZLDSH.toString());
        }

        memInfoCheckService.update(memInfoCheck);
    }

    /**
     * 密码修改（参数全通过base64编码）
     *
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return 错误提示消息（为空则表示修改成功）
     */
    public String updateMemberPwd(String oldPassword, String newPassword, String confirmPassword) {
        String errorMsg = "";
        if (isRightPassword(oldPassword)) {
            if ((StringUtils.trimToEmpty(newPassword)).equals(confirmPassword)) {
                String newPwd = new String(EncodeUtil.decodeBase64(newPassword));
                String md5Pwd = new Md5Hash(newPwd, null, 2).toString();
                Member member = new Member();
                member.setPassword(md5Pwd);
                member.setId(String.valueOf(ShiroUtil.getCreateID()));
                oldMemberService.updateMemInfo(member);

            } else {
                errorMsg = "两次输入的密码不一致";
            }
        } else {
            errorMsg = "密码输入错";
        }

        return errorMsg;
    }

    /**
     * 判断密码是当前登录者密码
     *
     * @param password
     * @return
     */
    private boolean isRightPassword(String password) {
        Member member = oldMemberService.findMemById(String.valueOf(ShiroUtil.getCreateID()));
        String md5Pwd = new Md5Hash(new String(EncodeUtil.decodeBase64(password)), null, 2).toString();

        return null != member && StringUtils.isNotBlank(member.getPassword()) ? member.getPassword().equals(md5Pwd) : false;
    }

    /**
     * 修改手机号码
     *
     * @param mobile
     * @param verifyCode
     * @return 错误提示消息（为空则表示修改成功）
     */
    public String updateMemberMobile(String mobile, String verifyCode) {
        String errorMsg = "";

        return errorMsg;
    }

    /**
     * 搜索供应商
     *
     * @param spec
     *            查询条件
     * @return map
     * @throws ServiceException
     */
    public Map<String, Object> searchSuppliers(SupplierSearchSpec spec) throws ServiceException {
        if (spec.getLimit() <= 0 || spec.getLimit() > 100) {
            spec.setLimit(12);
        }
        return membersService.searchSuppliers(spec);
    }

    /**
     * 根据会员ID查询会员信息
     */
    public Member findMemById(String id) {
        return memberMapper.findMemById(id);
    }

    /**
     * 获取企业荣誉资质信息
     *
     * @param id
     * @return
     */
    public List<CertificateRecord> certificateSearch(String id) {
        CertificateRecord certificateRecord = new CertificateRecord();
        certificateRecord.setMem_id(id);
        // 供应商资质
        certificateRecord.setType("1");
        certificateRecord.setIs_deleted(0);
        // 审核通过
        certificateRecord.setStatus("1");
        return oldMemberService.certificateSearch(certificateRecord);
    }

    /**
     * 留言
     * 
     * @param message
     * @return
     */
    public void saveMessage(Message message) {
        oldMemberService.saveMessage(message);
    }

}
