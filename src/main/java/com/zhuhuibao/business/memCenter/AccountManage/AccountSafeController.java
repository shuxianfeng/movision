package com.zhuhuibao.business.memCenter.AccountManage;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.ResourcePropertiesUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxx on 2016/3/11 0011.
 */
@RestController
public class AccountSafeController {
    private static final Logger log = LoggerFactory
            .getLogger(AccountSafeController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private DictionaryService ds;

    @Autowired
    private AccountService as;

    /**
     * 根据账号验证会员密码是否正确
     * @param req
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/rest/checkPwdById", method = RequestMethod.GET)
    public void checkPwdById(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String id = req.getParameter("id");
        //前台密码解密
        String pwd = new String(EncodeUtil.decodeBase64(req.getParameter("pwd")));
        String md5Pwd = new Md5Hash(pwd,null,2).toString();
        Member member = memberService.findMemById(id);

        //对比密码是否正确
        JsonResult result = new JsonResult();
        if(member!=null){
            if(!md5Pwd.equals(member.getPassword())){
                result.setCode(400);
                result.setMessage("密码不正确");
            }else{
                result.setCode(200);
            }
        }else{
            result.setCode(400);
            result.setMessage("账户不存在");
        }

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据账号验证会员密码是否正确
     * @param req
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/rest/checkPwdByAccount", method = RequestMethod.GET)
    public void checkPwdByAccount(HttpServletRequest req, HttpServletResponse response) throws IOException {
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
        Member mem = memberMapper.findMem(member);

        //对比密码是否正确
        JsonResult result = new JsonResult();
        if(mem!=null){
            if(!md5Pwd.equals(mem.getPassword())){
                result.setCode(400);
                result.setMessage("密码不正确");
            }else{
                result.setCode(200);
            }
        }else{
            result.setCode(400);
            result.setMessage("账户不存在");
        }

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 更新密码
     * @param req
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/rest/saveNewPwd", method = RequestMethod.POST)
    public void saveNewPwd(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String id = req.getParameter("id");
        String newPwd = new String(EncodeUtil.decodeBase64(req.getParameter("newPwd")));
        String md5Pwd = new Md5Hash(newPwd,null,2).toString();
        Member member = new Member();
        member.setPassword(md5Pwd);
        member.setId(Long.parseLong(id));

        JsonResult result = new JsonResult();
        int isUpdate = memberMapper.updateMember(member);
        if(isUpdate==1){
            result.setCode(200);
        }else{
            result.setCode(400);
            result.setMessage("更新密码失败");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 发送更改邮箱邮件
     * @param req
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/rest/sendChangeEmail", method = RequestMethod.POST)
    public void sendChangeEmail(HttpServletRequest req, HttpServletResponse response,Member member) throws IOException {
        log.debug("更改邮箱  email =="+member.getEmail());
        JsonResult result = new JsonResult();
        as.sendChangeEmail(member,ResourcePropertiesUtils.getValue("host.ip"));
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
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
