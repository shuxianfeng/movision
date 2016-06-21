package com.zhuhuibao.business.member.mc;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BaseException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.sms.SDKSendSms;
import com.zhuhuibao.utils.sms.SDKSendTaoBaoSMS;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 会员中心账户安全管理
 * Created by cxx on 2016/3/11 0011.
 */
@RestController
@RequestMapping("/rest/member/mc/user")
public class AccountSafeController {
    private static final Logger log = LoggerFactory.getLogger(AccountSafeController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private DictionaryService ds;

    @Autowired
    private AccountService as;

    @Autowired
    private MemberRegService memberRegService;

    @ApiOperation(value = "根据会员id验证会员密码是否正确", notes = "根据会员id验证会员密码是否正确", response = Response.class)
    @RequestMapping(value = "check_pwd_by_id", method = RequestMethod.GET)
    public Response checkPwdById(@RequestParam String pwd)  {
        Long memberId = ShiroUtil.getCreateID();
        //对比密码是否正确
        Response result = new Response();
        if(memberId!=null){
            //前台密码解密
            String md5Pwd = new Md5Hash(new String(EncodeUtil.decodeBase64(pwd)),null,2).toString();
            Member member = memberService.findMemById(String.valueOf(memberId));
            result = checkPwd(md5Pwd, member, result);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    /**
     * //对比密码是否正确
     * @param md5Pwd
     * @param member
     * @param result
     */
    private Response checkPwd(String md5Pwd, Member member, Response result) {
        if(member!=null){
            if(!md5Pwd.equals(member.getPassword())){
                throw new BusinessException(MsgCodeConstant.member_mcode_usernameorpwd_error,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_usernameorpwd_error)));
            }
        }else{
            throw new BusinessException(MsgCodeConstant.member_mcode_username_not_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
        }

        return  result;
    }

    @ApiOperation(value = "根据账号验证会员密码是否正确", notes = "根据账号验证会员密码是否正确", response = Response.class)
    @RequestMapping(value = "check_pwd_by_account", method = RequestMethod.GET)
    public Response checkPwdByAccount(@RequestParam String account,@RequestParam String pwd)  {
        //前台密码解密
        String password = new String(EncodeUtil.decodeBase64(pwd));
        String md5Pwd = new Md5Hash(password,null,2).toString();

        //根据账号查询会员信息
        Member member = new Member();
        if(account.contains("@")){
            member.setEmail(account);
        }else{
            member.setMobile(account);
        }
        Member mem = memberService.findMember(member);

        //对比密码是否正确
        Response result = new Response();
        result = checkPwd(md5Pwd, mem, result);
        return result;
    }

    @ApiOperation(value = "更新密码", notes = "更新密码", response = Response.class)
    @RequestMapping(value = "add_new_pwd", method = RequestMethod.POST)
    public Response saveNewPwd(@RequestParam String newPwd)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            String newPassword= new String(EncodeUtil.decodeBase64(newPwd));
            String md5Pwd = new Md5Hash(newPassword,null,2).toString();
            Member member = new Member();
            member.setPassword(md5Pwd);
            member.setId(String.valueOf(memberId));
            memberService.updateMemInfo(member);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "发送更改邮箱邮件", notes = "发送更改邮箱邮件", response = Response.class)
    @RequestMapping(value = "send_email", method = RequestMethod.POST)
    public Response sendChangeEmail(@RequestParam String email) {
        Response result = new Response();
        Member member1 = new Member();
        member1.setEmail(email);
        Member member = memberService.findMember(member1);
        if(member!=null){
            throw new BusinessException(MsgCodeConstant.member_mcode_mail_registered,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mail_registered)));
        }else {
            Long id = ShiroUtil.getCreateID();
            if(id!=null){
                as.sendChangeEmail(email,String.valueOf(id));
                String mail = ds.findMailAddress(email);
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
            }else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }
        return result;
    }

    @ApiOperation(value = "更改手机号", notes = "更改手机号", response = Response.class)
    @RequestMapping(value = "upd_mobile", method = RequestMethod.POST)
    public Response updateMobile(@RequestParam String mobile)  {
        Response result = new Response();
        Member member = new Member();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            member.setId(String.valueOf(memberId));
            member.setMobile(mobile);
            memberService.updateMemInfo(member);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "验证手机号是否存在", notes = "验证手机号是否存在", response = Response.class)
    @RequestMapping(value = "check_mobile_isExist", method = RequestMethod.POST)
    public Response checkMobile(@RequestParam String mobile)  {
        Response result = new Response();
        Member member = new Member();
        member.setMobile(mobile);
        Member member1 = memberService.findMember(member);
        if(member1!=null){
            throw new BaseException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
        }
        return result;
    }

    @ApiOperation(value = "点击邮箱链接更改邮箱", notes = "点击邮箱链接更改邮箱", response = Response.class)
    @RequestMapping(value = "upd_email", method = RequestMethod.GET)
    public ModelAndView updateEmail(@RequestParam String time,@RequestParam String email,@RequestParam String id)  {
        ModelAndView modelAndView = new ModelAndView();
        Member member = new Member();
        String decodeTime = new String (EncodeUtil.decodeBase64(time));
        String decodeId = new String (EncodeUtil.decodeBase64(id));
        Date currentTime = new Date();//获取当前时间
        Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(decodeTime,"yyyy-MM-dd HH:mm:ss"),5,1);
        String redirectUrl ="";
        if(currentTime.before(registerDate)){
            if(email != null & !email.equals(""))
            {
                String decodeVM = new String (EncodeUtil.decodeBase64(email));
                member.setEmail(decodeVM);
                member.setId(decodeId);
                memberService.updateMemInfo(member);
                redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email-active-bind.page");
            }
        }else{
            redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email-active-error.page");
        }
        RedirectView rv = new RedirectView(redirectUrl);
        modelAndView.setView(rv);
        return modelAndView;
    }

    @ApiOperation(value = "绑定手机时手机发送的验证码", notes = "绑定手机时手机发送的验证码", response = Response.class)
    @RequestMapping(value = "get_verifyCode", method = RequestMethod.GET)
    public Response getModifyBindMobileSMS(@ApiParam(value = "验证的手机号") @RequestParam String mobile,
                                           @ApiParam(value = "图形验证码") @RequestParam String imgCode) throws IOException, ApiException {
        log.debug("获得手机验证码  mobile=="+mobile);
        Subject currentUser = SecurityUtils.getSubject();
        Response response = new Response();
        Session sess = currentUser.getSession(true);
        String sessionImgCode = (String) sess.getAttribute("bindingMobile");
        if(imgCode.equalsIgnoreCase(sessionImgCode)) {
            // 生成随机字串
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4, VerifyCodeUtils.VERIFY_CODES_DIGIT);
            log.debug("verifyCode == " + verifyCode);
            //发送验证码到手机
            //SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
            Map<String, String> map = new LinkedHashMap<>();
            map.put("code", verifyCode);
            map.put("time", Constants.sms_time);
            Gson gson = new Gson();
            String json = gson.toJson(map);
            SDKSendSms.sendSMS(mobile, json, PropertiesUtils.getValue("modify_mobile_sms_template_code"));
//        SDKSendTaoBaoSMS.sendModifyBindMobileSMS(mobile, verifyCode, Constants.sms_time);
            Validateinfo info = new Validateinfo();
            info.setCreateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            info.setCheckCode(verifyCode);
            info.setAccount(mobile);
            memberRegService.inserValidateInfo(info);
            sess.setAttribute("s" + mobile, verifyCode);
        }
        return response;
    }

    @ApiOperation(value="绑定手机时图形验证码",notes="绑定手机时图形验证码",response = Response.class)
    @RequestMapping(value = "get_img_code", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute("bindingMobile", verifyCode);
    }
}
