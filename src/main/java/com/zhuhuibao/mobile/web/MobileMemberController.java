package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.service.MobileMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tongxinglong
 * @date 2016/12/2 0002.
 */
@RestController
@RequestMapping("/rest/m/member/site/")
public class MobileMemberController {
    @Autowired
    private MobileMemberService memberService;

    @ApiOperation(value = "会员信息详情", notes = "会员信息详情")
    @RequestMapping(value = "sel_member_details", method = RequestMethod.GET)
    public Response selSupplierDetails(@ApiParam(value = "供应商id") @RequestParam String id) {
        // 查询公司信息
        Member member = memberService.findMemById(id);
        return new Response(member);
    }
}
