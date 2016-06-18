package com.zhuhuibao.business.member.mc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.CertificateRecord;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.WorkType;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料维护
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
@RequestMapping("/rest/member/mc/individual")
public class IndividualController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "工作类别", notes = "工作类别", response = Response.class)
    @RequestMapping(value = "sel_workTypeList", method = RequestMethod.GET)
    public Response workTypeList()  {
        Response result = new Response();
        List<WorkType> workType = memberService.findIndividualWorkTypeList();
        result.setData(workType);
        return result;
    }

    @ApiOperation(value = "查询个人所有信息", notes = "查询个人所有信息", response = Response.class)
    @RequestMapping(value = "sel_mem_info", method = RequestMethod.GET)
    public Response info() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            Member member = memberService.findMemById(String.valueOf(memberId));
            result.setData(member);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "个人基本资料保存", notes = "个人基本资料保存", response = Response.class)
    @RequestMapping(value = "upd_mem_basic_info", method = RequestMethod.POST)
    public Response upd_mem_basic_info(@ModelAttribute Member member)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            member.setId(String.valueOf(memberId));
            //基本资料待审核
            member.setStatus(MemberConstant.MemberStatus.WSZLDSH.toString());
            memberService.updateMemInfo(member);
            Member loginMember = memberService.findMemById(String.valueOf(memberId));
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
                principal.setStatus(Integer.parseInt(loginMember.getStatus()));
                session.setAttribute("member", principal);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "个人实名认证保存", notes = "个人实名认证保存", response = Response.class)
    @RequestMapping(value = "upd_mem_realName_info", method = RequestMethod.POST)
    public Response upd_mem_realName_info(@ModelAttribute Member member)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            member.setId(String.valueOf(memberId));
            //实名认证待审核
            member.setStatus(MemberConstant.MemberStatus.SMRZDSH.toString());
            memberService.updateMemInfo(member);
            Member loginMember = memberService.findMemById(String.valueOf(memberId));
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
                principal.setStatus(Integer.parseInt(loginMember.getStatus()));
                session.setAttribute("member", principal);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "个人资质保存", notes = "个人资质保存", response = Response.class)
    @RequestMapping(value = "add_certificate", method = RequestMethod.POST)
    public Response certificateSave(String json)  {
        Response result = new Response();
        Gson gson=new Gson();
        List<CertificateRecord> rs= new ArrayList<CertificateRecord>();
        Type type = new TypeToken<ArrayList<CertificateRecord>>() {}.getType();
        rs = gson.fromJson(json, type);
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            memberService.deleteCertificate(String.valueOf(memberId));
            for(CertificateRecord record:rs){
                record.setMem_id(String.valueOf(memberId));
                memberService.saveCertificate(record);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }
}
