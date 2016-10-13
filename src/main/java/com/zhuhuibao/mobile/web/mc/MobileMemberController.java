package com.zhuhuibao.mobile.web.mc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
