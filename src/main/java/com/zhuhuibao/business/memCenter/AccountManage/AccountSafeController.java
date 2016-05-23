package com.zhuhuibao.business.memCenter.AccountManage;

import com.taobao.api.ApiException;
import com.zhuhuibao.common.constant.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ResourcePropertiesUtils;
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
    @RequestMapping(value = "/rest/checkPwdById", method = RequestMethod.GET)
    public JsonResult checkPwdById(HttpServletRequest req) throws Exception {
        String id = req.getParameter("id");
        //前台密码解密
        String pwd = new String(EncodeUtil.decodeBase64(req.getParameter("pwd")));
        String md5Pwd = new Md5Hash(pwd,null,2).toString();
        Member member = memberService.findMemById(id);

        //对比密码是否正确
        JsonResult result = new JsonResult();
        result = checkPwd(md5Pwd, member, result);

        return result;
    }

    /**
     * //对比密码是否正确
     * @param md5Pwd
     * @param member
     * @param result
     */
    private JsonResult checkPwd(String md5Pwd, Member member, JsonResult result) {
        if(member!=null){
            if(!md5Pwd.equals(member.getPassword())){
                result.setCode(400);
                result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_usernameorpwd_error)));
                result.setMsgCode(MsgCodeConstant.member_mcode_usernameorpwd_error);
            }else{
                result.setCode(200);
            }
        }else{
            result.setCode(400);
            result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_username_not_exist)));
            result.setMsgCode(MsgCodeConstant.member_mcode_username_not_exist);
        }

        return  result;
    }

    /**
     * 根据账号验证会员密码是否正确
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = "/rest/checkPwdByAccount", method = RequestMethod.GET)
    public JsonResult checkPwdByAccount(HttpServletRequest req) throws Exception {
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
        JsonResult result = new JsonResult();
        result = checkPwd(md5Pwd, mem, result);

        return result;
    }

    /**
     * 更新密码
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = "/rest/saveNewPwd", method = RequestMethod.POST)
    public JsonResult saveNewPwd(HttpServletRequest req) throws Exception {
        String id = req.getParameter("id");
        String newPwd = new String(EncodeUtil.decodeBase64(req.getParameter("newPwd")));
        String md5Pwd = new Md5Hash(newPwd,null,2).toString();
        Member member = new Member();
        member.setPassword(md5Pwd);
        member.setId(Long.parseLong(id));

        JsonResult result = new JsonResult();
        memberService.updateMember(member);
        return result;

    }

    /**
     * 发送更改邮箱邮件
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = "/rest/sendChangeEmail", method = RequestMethod.POST)
    public JsonResult sendChangeEmail(HttpServletRequest req) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "/rest/updateMobile", method = RequestMethod.POST)
    public JsonResult updateMobile(Member member) throws Exception {
        JsonResult result = new JsonResult();
        try {
            memberService.updateMember(member);
        }catch(Exception e){
            throw e;
        }

        return result;
    }

    /**
     * 验证手机号是否存在
     * @throws IOException
     */
    @RequestMapping(value = "/rest/checkMobile", method = RequestMethod.POST)
    public JsonResult checkMobile(Member member) throws IOException {
        JsonResult result = new JsonResult();
        try {
            Member member1 = memberService.findMemer(member);
            if(member1!=null){
                result.setCode(400);
                result.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_account_exist)));
                result.setMsgCode(MsgCodeConstant.member_mcode_account_exist);
            }else {
                result.setCode(200);
            }
        }catch(Exception e){
            log.error("checkMobile error");
        }

        return result;
    }

    /**
     * 点击邮箱链接更改邮箱
     * @param req
     * @throws IOException
     */
    @RequestMapping(value = "/rest/updateEmail", method = RequestMethod.GET)
    public ModelAndView updateEmail(HttpServletRequest req) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        Member member = new Member();
        try
        {
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
                    redirectUrl = ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("email-active-bind.page");
                }
            }else{
                redirectUrl = ResourcePropertiesUtils.getValue("host.ip")+"/"+ResourcePropertiesUtils.getValue("email-active-error.page");
            }
            RedirectView rv = new RedirectView(redirectUrl);
            modelAndView.setView(rv);
        }
        catch(Exception e)
        {
            log.error("email updateEmail error!",e);
        }
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
    @RequestMapping(value = "/rest/getModifyBindMobileSMS", method = RequestMethod.GET)
    public JsonResult getModifyBindMobileSMS(HttpServletRequest req) throws IOException, ApiException {
        String mobile = req.getParameter("mobile");
        log.debug("获得手机验证码  mobile=="+mobile);
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4,VerifyCodeUtils.VERIFY_CODES_DIGIT);
        log.debug("verifyCode == " + verifyCode);
        //发送验证码到手机
        //SDKSendTemplateSMS.sendSMS(mobile, verifyCode);
        SDKSendTaoBaoSMS.sendModifyBindMobileSMS(mobile, verifyCode, Constant.sms_time);
        Validateinfo info = new Validateinfo();
        info.setCreateTime(DateUtils.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
        info.setCheckCode(verifyCode);
        info.setAccount(mobile);
        memberRegService.inserValidateInfo(info);
        sess.setAttribute("s"+mobile, verifyCode);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(verifyCode);

        return jsonResult;
    }
}
