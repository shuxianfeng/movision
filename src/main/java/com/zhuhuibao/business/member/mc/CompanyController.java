package com.zhuhuibao.business.member.mc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.SuccessCaseConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memCenter.service.SuccessCaseService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * 企业资料维护
 * Created by cxx on 2016/6/17 0017.
 */
@RestController
@RequestMapping("/rest/member/mc/company")
public class CompanyController {

    @Autowired
    private MemberService memberService;

    @Autowired
    SuccessCaseService successCaseService;

    @ApiOperation(value = "查询企业所有信息", notes = "查询企业所有信息", response = Response.class)
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

    @ApiOperation(value = "企业基本资料保存", notes = "企业基本资料保存", response = Response.class)
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

    @ApiOperation(value = "企业实名认证保存", notes = "企业实名认证保存", response = Response.class)
    @RequestMapping(value = "upd_mem_realName_info", method = RequestMethod.POST)
    public Response upd_mem_realName_info(@ApiParam(value = "")@RequestParam String coBusLicNum,
                                          @RequestParam String companyBusinessLicenseImg)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        Member member = new Member();
        if(memberId!=null){
            member.setId(String.valueOf(memberId));
            //实名认证待审核
            member.setStatus(MemberConstant.MemberStatus.SMRZDSH.toString());
            member.setCoBusLicNum(coBusLicNum);
            member.setCompanyBusinessLicenseImg(companyBusinessLicenseImg);
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

    @ApiOperation(value = "企业资质保存", notes = "企业资质保存", response = Response.class)
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

    @ApiOperation(value = "企业资质查询", notes = "企业资质查询", response = Response.class)
    @RequestMapping(value = "sel_certificate", method = RequestMethod.GET)
    public Response sel_certificate()  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            CertificateRecord record = new CertificateRecord();
            record.setMem_id(String.valueOf(memberId));
            record.setIs_deleted(0);
            List<CertificateRecord> list = memberService.certificateSearch(record);
            result.setData(list);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "资质类型", notes = "资质类型", response = Response.class)
    @RequestMapping(value = "sel_certificateList", method = RequestMethod.GET)
    public Response certificateList(@RequestParam String type)  {
        Response response = new Response();
        List list = memberService.findCertificateList(type);
        response.setData(list);
        return response;
    }

    @ApiOperation(value = "企业性质", notes = "企业性质", response = Response.class)
    @RequestMapping(value = "sel_enterpriseTypeList", method = RequestMethod.GET)
    public Response enterpriseTypeList()  {
        Response result = new Response();
        List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
        result.setData(enterpriseType);
        return result;
    }

    @ApiOperation(value = "企业身份", notes = "企业身份", response = Response.class)
    @RequestMapping(value = "sel_identityList", method = RequestMethod.GET)
    public Response identityList()  {
        Response result = new Response();
        List<Identity> identity = memberService.findIdentityList();
        result.setData(identity);
        return result;
    }

    @ApiOperation(value = "人员规模", notes = "人员规模", response = Response.class)
    @RequestMapping(value = "sel_employeeSizeList", method = RequestMethod.GET)
    public Response employeeSizeList()  {
        Response result = new Response();
        List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
        result.setData(employeeSizeList);
        return result;
    }

    @ApiOperation(value = "企业发布成功案例", notes = "企业发布成功案例", response = Response.class)
    @RequestMapping(value = "add_successCase", method = RequestMethod.POST)
    public Response add_successCase(@ModelAttribute SuccessCase successCase) throws Exception {
        Response result = new Response();
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            successCase.setCreateid(String.valueOf(createid));
            successCaseService.addSuccessCase(successCase);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "我（企业）发布的成功案例", notes = "我（企业）发布的成功案例", response = Response.class)
    @RequestMapping(value = "sel_successCaseList", method = RequestMethod.GET)
    public Response sel_successCaseList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                        @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String pageNo,
                                        @RequestParam(required = false) String pageSize) throws Exception {
        Response result = new Response();
        //设定默认分页pageSize
        if (com.zhuhuibao.utils.pagination.util.StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (com.zhuhuibao.utils.pagination.util.StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            map.put("createid",String.valueOf(createid));
            List<Map<String,String>> list = successCaseService.findAllSuccessCaseList(pager,map);
            result.setData(list);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "根据id查询成功案例信息", notes = "根据id查询成功案例信息", response = Response.class)
    @RequestMapping(value = "sel_successCase", method = RequestMethod.GET)
    public Response sel_successCase(@RequestParam String id) {
        Response result = new Response();
        SuccessCase successCase = successCaseService.querySuccessCaseById(id);
        result.setData(successCase);
        return result;
    }

    @ApiOperation(value = "编辑更新成功案例信息", notes = "编辑更新成功案例信息", response = Response.class)
    @RequestMapping(value = "upd_successCase", method = RequestMethod.GET)
    public Response upd_successCase(@ModelAttribute SuccessCase successCase) {
        Response result = new Response();
        successCaseService.updateSuccessCase(successCase);
        result.setData(successCase);
        return result;
    }

    @ApiOperation(value = "删除成功案例信息", notes = "删除成功案例信息", response = Response.class)
    @RequestMapping(value = "del_successCase", method = RequestMethod.GET)
    public Response del_successCase(@ApiParam(value = "成功案例ids,逗号隔开") @RequestParam String ids) {
        Response result = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            SuccessCase successCase = new SuccessCase();
            successCase.setIs_deleted(SuccessCaseConstant.SUCCESS_DELETE_ONE);
            successCase.setId(id);
            successCaseService.updateSuccessCase(successCase);
        }
        return result;
    }

}
