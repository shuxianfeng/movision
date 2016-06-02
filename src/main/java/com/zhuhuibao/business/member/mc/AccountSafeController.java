package com.zhuhuibao.business.member.mc;

import com.taobao.api.ApiException;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员中心账户安全管理
 * Created by cxx on 2016/3/11 0011.
 */
@RestController
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

    /**
     * 根据账号验证会员密码是否正确
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/checkPwdById","/rest/member/mc/user/check_pwd_id"}, method = RequestMethod.GET)
    public Response checkPwdById(HttpServletRequest req)  {
        String id = req.getParameter("id");
        //前台密码解密
        String pwd = new String(EncodeUtil.decodeBase64(req.getParameter("pwd")));
        String md5Pwd = new Md5Hash(pwd,null,2).toString();
        Member member = memberService.findMemById(id);

        //对比密码是否正确
        Response result = new Response();
        result = checkPwd(md5Pwd, member, result);

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
            }else{
                result.setCode(200);
            }
        }else{
            throw new BusinessException(MsgCodeConstant.member_mcode_username_not_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
        }

        return  result;
    }

    /**
     * 根据账号验证会员密码是否正确
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/checkPwdByAccount","/rest/member/mc/user/check_pwd_account"}, method = RequestMethod.GET)
    public Response checkPwdByAccount(HttpServletRequest req)  {
        //账号：手机或邮箱
        String account = req.getParameter("account");
        //前台密码解密
        String pwd = new String(EncodeUtil.decodeBase64(req.getParameter("pwd")));
        String md5Pwd = new Md5Hash(pwd,null,2).toString();

        //根据账号查询会员信息
        Member member = new Member();
        if(account.contains("@")){
            member.setEmail("account");
        }else{
            member.setMobile("account");
        }
        Member mem = memberService.findMember(member);

        //对比密码是否正确
        Response result = new Response();
        result = checkPwd(md5Pwd, mem, result);
        return result;
    }

    /**
     * 更新密码
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/saveNewPwd","/rest/member/mc/user/add_new_pwd"}, method = RequestMethod.POST)
    public Response saveNewPwd(HttpServletRequest req)  {
        String id = req.getParameter("id");
        String newPwd = new String(EncodeUtil.decodeBase64(req.getParameter("newPwd")));
        String md5Pwd = new Md5Hash(newPwd,null,2).toString();
        Member member = new Member();
        member.setPassword(md5Pwd);
        member.setId(Long.parseLong(id));
        Response result = new Response();
        memberService.updateMember(member);
        return result;

    }

    /**
     * 发送更改邮箱邮件
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/sendChangeEmail","/rest/member/mc/user/send_email"}, method = RequestMethod.POST)
    public Response sendChangeEmail(HttpServletRequest req) {
        Response result = new Response();
        String email = req.getParameter("email");
        Member member1 = new Member();
        member1.setEmail(email);
        Member member = memberService.findMemer(member1);
        if(member!=null){
            result.setCode(400);
            result.setMessage("该邮箱已存在");
        }else {
            String id = req.getParameter("id");
            log.debug("更改邮箱  email =="+ email);

            as.sendChangeEmail(email,id);
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
            result.setCode(200);
        }

        return result;
    }

    /**
     * 更改手机号
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/updateMobile","/rest/member/mc/user/upd_mobile"}, method = RequestMethod.POST)
    public Response updateMobile(Member member)  {
        Response result = new Response();
        memberService.updateMember(member);
        return result;
    }

    /**
     * 验证手机号是否存在
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/checkMobile","/rest/member/mc/user/check_mobile_isExist"}, method = RequestMethod.POST)
    public Response checkMobile(Member member)  {
        Response result = new Response();
        Member member1 = memberService.findMemer(member);
        if(member1!=null){
            throw new BaseException(MsgCodeConstant.member_mcode_account_exist,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
        }else {
            result.setCode(200);
        }
        return result;
    }

    /**
     * 点击邮箱链接更改邮箱
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/updateEmail","/rest/member/mc/user/upd_email"}, method = RequestMethod.GET)
    public ModelAndView updateEmail(HttpServletRequest req)  {
        ModelAndView modelAndView = new ModelAndView();
        Member member = new Member();
        String email = req.getParameter("email");//获取email
        String decodeTime = new String (EncodeUtil.decodeBase64(req.getParameter("time")));
        String decodeId = new String (EncodeUtil.decodeBase64(req.getParameter("id")));
        Date currentTime = new Date();//获取当前时间
        Date registerDate = DateUtils.date2Sub(DateUtils.str2Date(decodeTime,"yyyy-MM-dd HH:mm:ss"),5,1);
        String redirectUrl ="";
        if(currentTime.before(registerDate)){
            if(email != null & !email.equals(""))
            {
                String decodeVM = new String (EncodeUtil.decodeBase64(email));
                member.setEmail(decodeVM);
                member.setId(Long.parseLong(decodeId));
                memberService.updateMember(member);
                redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email-active-bind.page");
            }
        }else{
            redirectUrl = PropertiesUtils.getValue("host.ip")+"/"+ PropertiesUtils.getValue("email-active-error.page");
        }
        RedirectView rv = new RedirectView(redirectUrl);
        modelAndView.setView(rv);
        return modelAndView;
    }

    /**
     * 绑定手机时手机发送的验证码
     * @param req
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     * @throws ApiException
     */
    @RequestMapping(value = {"/rest/getModifyBindMobileSMS","/rest/member/mc/user/get_verifyCode"}, method = RequestMethod.GET)
    public Response getModifyBindMobileSMS(HttpServletRequest req) throws IOException, ApiException {
        String mobile = req.getParameter("mobile");
        log.debug("获得手机验证码  mobile=="+mobile);
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        //发送验证码到手机
        //SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
        SDKSendTaoBaoSMS.sendModifyBindMobileSMS(mobile, verifyCode, Constants.sms_time);
        Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);
        memberRegService.inserValidateInfo(info);
        sess.setAttribute("s"+mobile, verifyCode);
        Response response = new Response();
        response.setData(verifyCode);

        return response;
    }
}
