package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
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

    @ApiOperation(value = "更新登录人member_check信息信息", notes = "获取登录人member_check信息", response = Response.class)
    @RequestMapping(value = "/upd_member_check", method = RequestMethod.POST)
    public Response updMemberCheck(@ModelAttribute MemInfoCheck memberChk) throws Exception {
        Response response = new Response();
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        if (null != loginMember) {
            // 更新memberChk信息
            memberChk.setId(loginMember.getId());
            memberService.updateMemberInfoCheck(memberChk);

            // 更新session中数据状态
            ShiroUtil.updateShiroUser(memberChk.getStatus(), memberChk.getIdentify());
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

    @ApiOperation(value = "手机号码修改验证", notes = "手机号码修改", response = Response.class)
    @RequestMapping(value = "/sel_chk_old_mobile", method = RequestMethod.POST)
    public Response chkOldMobile(@ApiParam(value = "验证码") @RequestParam String verifyCode) throws Exception {
        Response response = new Response();
        // 验证 验证码是否正确

        // 修改手机号码

        return response;
    }

    @ApiOperation(value = "手机号码修改", notes = "手机号码修改", response = Response.class)
    @RequestMapping(value = "/upd_mobile", method = RequestMethod.POST)
    public Response updMobile(@ApiParam(value = "手机号") @RequestParam String mobile, @ApiParam(value = "验证码") @RequestParam String verifyCode) throws Exception {
        Response response = new Response();
        // 验证 验证码是否正确

        // 修改手机号码

        return response;
    }
}
