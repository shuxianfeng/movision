package com.zhuhuibao.mobile.web.mc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.service.MobileMemberService;

/**
 * 会员信息Controller
 * 
 * @author tongxinglong
 * @date 2016/10/12 0012.
 */
@RestController
@RequestMapping("/rest/m/member/mc")
public class MobileMemberController {

    @Autowired
    private MobileMemberService memberService;

    @ApiOperation(value = "获取登录人member_check信息", notes = "获取登录人member_check信息", response = Response.class)
    @RequestMapping(value = "/sel_member_check", method = RequestMethod.GET)
    public Response selMemberCheck() throws Exception {
        Response response = new Response();
        response.setData(memberService.getMemInfoCheckById(ShiroUtil.getCreateID()));

        return response;
    }

    @ApiOperation(value = "更新登录人member_check信息信息", notes = "更新登录人member_check信息", response = Response.class)
    @RequestMapping(value = "/upd_member_check", method = RequestMethod.POST)
    public Response updMemberCheck(@ModelAttribute MemInfoCheck memberChk) throws Exception {
        Response response = new Response();
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        if (null != loginMember) {
            // 更新memberChk信息
            memberService.updateMemberInfoCheck(memberChk);

            // 更新session中数据状态
            ShiroUtil.updateShiroUser(memberChk.getStatus(),memberChk.getIdentify(),memberChk.getHeadShot());
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

    @ApiOperation(value = "密码修改", notes = "密码修改", response = Response.class)
    @RequestMapping(value = "/upd_pwd", method = RequestMethod.POST)
    public Response updMemberPwdCheck(@ApiParam(value = "原密码(base64)") @RequestParam String oldPassword, @ApiParam(value = "新密码(base64)") @RequestParam String newPassword,
            @ApiParam(value = "确认密码(base64)") @RequestParam String confirmPassword) throws Exception {
        Response response = new Response();
        String errorMsg = memberService.updateMemberPwd(oldPassword, newPassword, confirmPassword);
        if (StringUtils.isNotBlank(errorMsg)) {
            response.setCode(400);
            response.setMessage(errorMsg);
        }

        return response;
    }

    @ApiOperation(value = "校验手机号", notes = "校验手机号", response = Response.class)
    @RequestMapping(value = "/verifyMobile", method = RequestMethod.POST)
    public Response verifyMobile(@ApiParam(value = "手机号") @RequestParam String mobile) throws Exception {
    	
        return memberService.validateMobile(mobile);
    }
    
    @ApiOperation(value = "根据图形验证码发送手机验证码", notes = "根据图形验证码发送手机验证码", response = Response.class)
    @RequestMapping(value = "/sendMobileVerifyCodeByImgCode", method = RequestMethod.GET)
    public Response sendMobileVerifyCodeByImgCode(
    		@ApiParam(value = "图形验证码") @RequestParam String imgVerifyCode,
    		@ApiParam(value = "验证的手机号") @RequestParam String mobile) throws Exception {
    	
    	Response res = new Response();
        if(memberService.verifyImgCode(imgVerifyCode)){	//校验图形验证码
        	//发送手机验证码
        	res = memberService.sendMobileVerifyCode(mobile);
        }else{
        	res.setCode(400);
        	res.setMessage("验证码输入错误！");
        }
        return res;
    }
    
    @ApiOperation(value = "生成绑定手机时图形验证码", notes = "生成绑定手机时图形验证码", response = Response.class)
    @RequestMapping(value = "/get_img_code", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) throws IOException {
    	
    	memberService.getImgCode(response);
    }
    
    @ApiOperation(value = "绑定手机", notes = "绑定手机", response = Response.class)
    @RequestMapping(value = "/bindMobile", method = RequestMethod.POST)
    public Response bindMobile(
    		@ApiParam(value = "手机验证码") @RequestParam String code,
    		@ApiParam(value = "验证的手机号") @RequestParam String mobile) throws Exception {
    	
        return memberService.bindMobile(mobile, code);
    }
    
    @ApiOperation(value = "获取手机验证码", notes = "获取手机验证码", response = Response.class)
    @RequestMapping(value = "/getSMSVerifyCode", method = RequestMethod.GET)
    public Response getSMSVerifyCode() throws Exception {
    	
        return memberService.getSMSVerifyCode();
    }
    
    @ApiOperation(value = "校验旧手机的短信验证码", notes = "校验旧手机的短信验证码", response = Response.class)
    @RequestMapping(value = "/sel_chk_old_mobile", method = RequestMethod.POST)
    public Response chkOldMobile(@ApiParam(value = "验证码") @RequestParam String code) throws Exception {
        // 验证 验证码是否正确
        return memberService.chkOldMobile(code);
    }

    @ApiOperation(value = "根据新手机号获取图形验证码", notes = "根据新手机号获取图形验证码", response = Response.class)
    @RequestMapping(value = "/gen_ImgCode_By_new_mobile", method = RequestMethod.GET)
    public Response genImgCodeByNewMobile(
    		HttpServletResponse response,
    		@ApiParam(value = "新手机号") @RequestParam String mobile) throws Exception {
    	
        return memberService.genImgCodeByNewMobile(response, mobile);
    }
    
}
