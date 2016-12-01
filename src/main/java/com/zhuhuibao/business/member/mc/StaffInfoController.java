package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cxx on 2016/6/20 0020.
 */
@RestController
@RequestMapping("/rest/member/mc/staff")
public class StaffInfoController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "查询账号持有人信息", notes = "查询账号持有人信息", response = Response.class)
    @RequestMapping(value = "sel_mem_info", method = RequestMethod.GET)
    public Response info() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Map map = new HashMap();
            Member member = memberService.findMemById(String.valueOf(memberId));
            if("0".equals(member.getEnterpriseEmployeeParentId())){
                if(member.getMobile()!=null){
                    map.put("companyAccount",member.getMobile());
                }else {
                    map.put("companyAccount",member.getEmail());
                }
            }else {
                Member company = memberService.findMemById(member.getEnterpriseEmployeeParentId());

                if(company.getMobile()!=null){
                    map.put("companyAccount",company.getMobile());
                }else {
                    map.put("companyAccount",company.getEmail());
                }
            }

            map.put("companyName",member.getEnterpriseName());
            String role = memberService.queryWorkTypeById(member.getWorkType().toString());
            map.put("role",role);
            map.put("enterpriseLinkman",member.getEnterpriseLinkman());
            map.put("enterpriseLinkman",member.getEnterpriseLinkman());
            map.put("sex",member.getSex());
            map.put("enterpriseLMDep",member.getEnterpriseLMDep());
            map.put("fixedTelephone",member.getFixedTelephone());
            map.put("fixedMobile",member.getFixedMobile());
            map.put("QQ",member.getQQ());
            result.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "账号持有人信息保存", notes = "账号持有人信息保存", response = Response.class)
    @RequestMapping(value = "upd_mem_info", method = RequestMethod.POST)
    public Response upd_mem_info(@RequestParam(required = false) String enterpriseLinkman,
                                 @RequestParam(required = false) String sex,
                                 @RequestParam(required = false) String enterpriseLMDep,
                                 @RequestParam(required = false) String fixedTelephone,
                                 @RequestParam(required = false) String fixedMobile,
                                 @RequestParam(required = false) String QQ)  {
        Response result = new Response();
        Member member = new Member();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            member.setId(String.valueOf(memberId));
            member.setEnterpriseLinkman(enterpriseLinkman);
            member.setSex(sex);
            member.setEnterpriseLMDep(enterpriseLMDep);
            member.setFixedTelephone(fixedTelephone);
            member.setFixedMobile(fixedMobile);
            member.setQQ(QQ);
            memberService.updateMemInfo(member);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }
}
