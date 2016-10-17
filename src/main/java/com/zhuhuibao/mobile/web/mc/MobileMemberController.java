package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.MemInfoCheck;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
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
    public Response updMemberPwdCheck(@ApiParam(value = "原密码") @RequestParam String oldPassword, @ApiParam(value = "新密码") @RequestParam String newPassword,
            @ApiParam(value = "确认密码") @RequestParam String crmPassword) throws Exception {
        Response response = new Response();
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        if (null != loginMember) {


        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }
}
