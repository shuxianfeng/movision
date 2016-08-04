package com.zhuhuibao.business.member.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.SuccessCaseConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemRealCheckService;
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

    @Autowired
    MemInfoCheckService memInfoCheckService;

    @Autowired
    MemRealCheckService memRealCheckService;

    @ApiOperation(value = "查询企业基本信息", notes = "查询企业基本信息", response = Response.class)
    @RequestMapping(value = "sel_mem_basic_info", method = RequestMethod.GET)
    public Response basicInfo() {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            MemInfoCheck  member = memInfoCheckService.findMemById(String.valueOf(memberId));
            Map<String,Object> map = new HashMap<>();
            map.put("enterpriseName",member.getEnterpriseName());
            map.put("enterpriseType",member.getEnterpriseType());
            map.put("enterpriseTypeName",member.getEnterpriseTypeName());
            map.put("identify",member.getIdentify());
            map.put("identifyName",member.getIdentifyName());
            map.put("enterpriseCreaterTime",member.getEnterpriseCreaterTime());
            map.put("registerCapital",member.getRegisterCapital());
            map.put("currency",member.getCurrency());
            map.put("employeeNumber",member.getEmployeeNumber());
            map.put("employeeNumberName",member.getEmployeeNumberName());
            map.put("province",member.getProvince());
            map.put("provinceName",member.getProvinceName());
            map.put("city",member.getCity());
            map.put("cityName",member.getCityName());
            map.put("area",member.getArea());
            map.put("areaName",member.getAreaName());
            map.put("address",member.getAddress());
            map.put("enterpriseLogo",member.getEnterpriseLogo());
            map.put("headShot",member.getHeadShot());
            map.put("saleProductDesc",member.getSaleProductDesc());
            map.put("enterpriseDesc",member.getEnterpriseDesc());
            map.put("enterpriseProvince",member.getEnterpriseProvince());
            map.put("enterpriseProvinceName",member.getEnterpriseProvinceName());
            map.put("enterpriseCity",member.getEnterpriseCity());
            map.put("enterpriseCityName",member.getEnterpriseCityName());
            map.put("enterpriseArea",member.getEnterpriseArea());
            map.put("enterpriseAreaName",member.getEnterpriseAreaName());
            map.put("enterpriseAddress",member.getEnterpriseAddress());
            map.put("enterpriseTelephone",member.getEnterpriseTelephone());
            map.put("enterpriseFox",member.getEnterpriseFox());
            map.put("enterpriseWebSite",member.getEnterpriseWebSite());
            map.put("status",member.getStatus());
            map.put("reason",member.getReason());
            result.setData(map);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "查询企业实名信息", notes = "查询企业实名信息", response = Response.class)
    @RequestMapping(value = "sel_mem_realName_info", method = RequestMethod.GET)
    public Response realNameInfo() {
        Map<String,String> map;
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            MemRealCheck member = memRealCheckService.findMemById(String.valueOf(memberId));
            map = new HashMap<>();
            map.put("companyBusinessLicenseImg",member.getCompanyBusinessLicenseImg());
            map.put("status", String.valueOf(member.getStatus()));
            map.put("reason",member.getReason());
            map.put("enterpriseName",member.getEnterpriseName());
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return new Response(map);
    }

    @ApiOperation(value = "企业基本资料保存", notes = "企业基本资料保存", response = Response.class)
    @RequestMapping(value = "upd_mem_basic_info", method = RequestMethod.POST)
    public Response upd_mem_basic_info(@ModelAttribute MemInfoCheck member)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        if(memberId!=null){
            member.setId(memberId);
            //基本资料待审核
            if(loginMember.getStatus() != MemberConstant.MemberStatus.WJH.intValue()
                    ||loginMember.getStatus() != MemberConstant.MemberStatus.ZX.intValue()){
                member.setStatus(MemberConstant.MemberStatus.WSZLDSH.intValue());
            }
            memInfoCheckService.update(member);
            Member mem = memberService.findMemById(String.valueOf(memberId));
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
                principal.setStatus(Integer.parseInt(mem.getStatus()));
                principal.setIdentify(loginMember.getIdentify());
                session.setAttribute("member", principal);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "企业实名认证保存", notes = "企业实名认证保存", response = Response.class)
    @RequestMapping(value = "upd_mem_realName_info", method = RequestMethod.POST)
    public Response upd_mem_realName_info(@ApiParam("企业名称")@RequestParam String enterpriseName,
                                          @ApiParam("营业执照图片URL")@RequestParam String companyBusinessLicenseImg)  {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        ShiroRealm.ShiroUser loginMember = ShiroUtil.getMember();
        MemRealCheck member = new MemRealCheck();
        if(memberId!=null){
            member.setId(memberId);
            //实名认证待审核
            if(loginMember.getStatus() != MemberConstant.MemberStatus.WJH.intValue()
                    ||loginMember.getStatus() != MemberConstant.MemberStatus.ZX.intValue()){
                member.setStatus(MemberConstant.MemberStatus.SMRZDSH.intValue());
            }
            member.setEnterpriseName(enterpriseName);
            member.setCompanyBusinessLicenseImg(companyBusinessLicenseImg);
            memRealCheckService.update(member);
            Member mem = memberService.findMemById(String.valueOf(memberId));
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession(false);
            if (session != null) {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
                principal.setStatus(Integer.parseInt(mem.getStatus()));
                session.setAttribute("member", principal);
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "企业资质保存", notes = "企业资质保存", response = Response.class)
    @RequestMapping(value = "add_certificate", method = RequestMethod.POST)
    public Response add_certificate(@ApiParam(value = "证书编号")@RequestParam String certificate_number,
                                    @ApiParam(value = "证书id")@RequestParam String certificate_id,
                                    @ApiParam(value = "资质名称")@RequestParam String certificate_name,
                                    @ApiParam(value = "资质等级")@RequestParam(required = false) String certificate_grade,
                                    @ApiParam(value = "资质证书图片url")@RequestParam String certificate_url,
                                    @ApiParam(value = "资质类型：1：供应商资质；2：工程商资质；")@RequestParam String type)  {
        Response result = new Response();
        CertificateRecord record = new CertificateRecord();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            record.setMem_id(String.valueOf(memberId));
            record.setType(type);
            record.setCertificate_grade(certificate_grade);
            record.setCertificate_id(certificate_id);
            record.setCertificate_name(certificate_name);
            record.setCertificate_number(certificate_number);
            record.setCertificate_url(certificate_url);
            memberService.saveCertificate(record);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "企业资质编辑", notes = "企业资质编辑", response = Response.class)
    @RequestMapping(value = "upd_certificate", method = RequestMethod.POST)
    public Response update_certificate(@RequestParam String id,
                                    @RequestParam String certificate_number,
                                    @RequestParam String certificate_id,
                                    @RequestParam String certificate_name,
                                    @RequestParam(required = false) String certificate_grade,
                                    @RequestParam String certificate_url)  {
        Response result = new Response();
        CertificateRecord record = new CertificateRecord();
        Long memberId = ShiroUtil.getCreateID();
        if(memberId!=null){
            record.setId(id);
            record.setCertificate_grade(certificate_grade);
            record.setCertificate_id(certificate_id);
            record.setCertificate_name(certificate_name);
            record.setCertificate_number(certificate_number);
            record.setCertificate_url(certificate_url);
            record.setStatus("0");
            memberService.updateCertificate(record);
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
            CertificateRecord record1 = new CertificateRecord();
            record1.setMem_id(String.valueOf(memberId));
            record1.setIs_deleted(0);
            record1.setType("1");
            List<CertificateRecord> list1 = memberService.certificateSearch(record1);

            CertificateRecord record2 = new CertificateRecord();
            record2.setMem_id(String.valueOf(memberId));
            record2.setIs_deleted(0);
            record2.setType("2");
            List<CertificateRecord> list2 = memberService.certificateSearch(record2);

            Map<String,Object> map = new HashMap<>();
            map.put("supplier",list1);
            map.put("contractor",list2);
            result.setData(map);
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
        Paging<Map<String,String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Long createid = ShiroUtil.getCreateID();
        if(createid!=null){
            map.put("createid",String.valueOf(createid));
            List<Map<String,String>> list = successCaseService.findAllSuccessCaseList(pager,map);
            pager.result(list);
            result.setData(pager);
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
    @RequestMapping(value = "upd_successCase", method = RequestMethod.POST)
    public Response upd_successCase(@ModelAttribute SuccessCase successCase) {
        Response result = new Response();
        successCase.setStatus("0");
        successCaseService.updateSuccessCase(successCase);
        return result;
    }

    @ApiOperation(value = "删除成功案例信息", notes = "删除成功案例信息", response = Response.class)
    @RequestMapping(value = "del_successCase", method = RequestMethod.POST)
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
