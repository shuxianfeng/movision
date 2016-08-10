package com.zhuhuibao.business.oms.member;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.OmsLoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MemberConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.OmsMemBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.MemInfoCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemRealCheckService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.OmsMemService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 会员管理
 *
 * @author cxx
 */
@RestController
@RequestMapping("/rest/member/oms/base/")
@Api(value = "MemberOmsController", description = "运营平台会员管理")
public class MemberOmsController {

    private static final Logger log = LoggerFactory.getLogger(MemberOmsController.class);

    @Autowired
    private OmsMemService omsMemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    MemInfoCheckService infoCheckService;

    @Autowired
    MemRealCheckService realCheckService;

    @ApiOperation(value = "查询会员（分页）", notes = "查询会员（分页）", response = Response.class)
    @RequestMapping(value = "sel_allMember", method = RequestMethod.GET)
    public Response getAllMemInfo(@ModelAttribute OmsMemBean member,
                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Paging<OmsMemBean> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        return omsMemService.getAllMemInfo(pager, member);
    }

    @ApiOperation(value = "查询审核会员（分页）", notes = "查询审核会员（分页）", response = Response.class)
    @RequestMapping(value = "sel_all_ckmem", method = RequestMethod.GET)
    public Response getAllcheckMem(@ModelAttribute OmsMemBean member,
                                   @RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize) {
        log.debug("查询审核会员...");

        Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String, Object>> list = omsMemService.getAllcheckMemInfo(pager, member);
        pager.result(list);
        return new Response(pager);
    }

    @ApiOperation(value = "查询全部会员资质（分页）", notes = "查询全部会员资质（分页）", response = Response.class)
    @RequestMapping(value = "sel_allCertificate", method = RequestMethod.GET)
    public Response getAllMemCertificate(@ModelAttribute OmsMemBean member,
                                         @RequestParam(required = false, defaultValue = "1") String pageNo,
                                         @RequestParam(required = false, defaultValue = "10") String pageSize) {

        Paging<OmsMemBean> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        return omsMemService.getAllMemCertificate(pager, member);
    }

    @RequestMapping(value = "sel_certificate", method = RequestMethod.GET)
    @ApiOperation(value = "会员资质查询", notes = "会员资质查询", response = Response.class)
    public Response queryCertificateById(@RequestParam String id) throws IOException {
        Response response = new Response();
        CertificateRecord certificateRecord = omsMemService.queryCertificateById(id);
        response.setData(certificateRecord);
        return response;
    }

    @RequestMapping(value = "upd_mem_status", method = RequestMethod.POST)
    @ApiOperation(value = "会员状态审核", notes = "会员状态审核", response = Response.class)
    public Response updateStatus(@ApiParam(value = "会员信息") @ModelAttribute(value = "member") Member member) {
        Response result = new Response();
        memberService.updateMemInfo(member);
        return result;
    }

    @OmsLoginAccess
    @RequestMapping(value = "upd_mem_data", method = RequestMethod.POST)
    @ApiOperation(value = "完善资料审核", notes = "完善资料审核", response = Response.class)
    public Response updateMemData(@ApiParam(value = "会员基本资料信息") @ModelAttribute(value = "member") MemInfoCheck member) {

        String status = infoCheckService.getStatusById(member.getId());
        if (!status.equals(MemberConstant.MemberStatus.WSZLDSH.toString())) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "非待审核状态");
        }

        memberService.updateMemData(member);

        return new Response();
    }

    @RequestMapping(value = "upd_memreal_data", method = RequestMethod.POST)
    @ApiOperation(value = "实名认证审核", notes = "实名认证审核", response = Response.class)
    public Response updateMemRealData(@ApiParam(value = "会员实名认证信息") @ModelAttribute MemRealCheck realCheck) {

        String lastStatus = realCheckService.getStatusById(realCheck.getId());
        if (!lastStatus.equals(MemberConstant.MemberStatus.SMRZDSH.toString())) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "非待审核状态");
        }

        Integer status = realCheck.getStatus();
        if(status == null){
            throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR,"状态不能为空");
        }
        if (MemberConstant.MemberStatus.SMRZYJJ.intValue() == status) {
            //拒绝理由必填
            String reason = realCheck.getReason();
            if (StringUtils.isEmpty(reason)) {
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "请填写拒绝理由");
            } else {
                realCheck.setReason(reason);
            }
        }

        memberService.updateMemRealData(realCheck);

        return new Response();
    }

    @RequestMapping(value = "upd_certificate_status", method = RequestMethod.POST)
    @ApiOperation(value = "资质审核", notes = "资质审核", response = Response.class)
    public Response updateCertificate(@ApiParam(value = "资质信息") @ModelAttribute(value = "record") CertificateRecord record) {
        Response result = new Response();
        memberService.updateCertificate(record);
        return result;
    }

    @ApiOperation(value = "企业身份", notes = "企业身份", response = Response.class)
    @RequestMapping(value = "sel_identityList", method = RequestMethod.GET)
    public Response identityList() {
        Response result = new Response();
        List<Identity> identity = memberService.findIdentityList();
        result.setData(identity);
        return result;
    }

    @ApiOperation(value = "查询企业或用户详情", notes = "查询企业或用户详情", response = Response.class)
    @RequestMapping(value = "sel_memberDetail", method = RequestMethod.GET)
    public Response selMemberDetail(@ApiParam(value = "会员ID") @RequestParam String mem_id) {
        Member member = memberService.findMemById(mem_id);
        return new Response(member);
    }

    @ApiOperation(value = "查询用户资料审核详情", notes = "查询用户资料审核详情", response = Response.class)
    @RequestMapping(value = "sel_meminfo_ck", method = RequestMethod.GET)
    public Response findMeminfoCheck(@ApiParam(value = "会员ID") @RequestParam String id) {

        Map<String, Object> map = memberService.findMeminfoCheck(id);

        return new Response(map);
    }

    @ApiOperation(value = "查询用户实名认证详情", notes = "查询用户实名认证详情", response = Response.class)
    @RequestMapping(value = "sel_memreal_ck", method = RequestMethod.GET)
    public Response findMemrealCheck(@ApiParam(value = "会员ID") @RequestParam String id) {

        Map<String, Object> map = memberService.findMemrealCheck(id);

        return new Response(map);
    }


    @ApiOperation(value = "企业性质", notes = "企业性质", response = Response.class)
    @RequestMapping(value = "sel_enterpriseTypeList", method = RequestMethod.GET)
    public Response enterpriseTypeList() {
        Response result = new Response();
        List<EnterpriseType> enterpriseType = memberService.findEnterpriseTypeList();
        result.setData(enterpriseType);
        return result;
    }

    @ApiOperation(value = "工作类别", notes = "工作类别", response = Response.class)
    @RequestMapping(value = "sel_workTypeList", method = RequestMethod.GET)
    public Response workTypeList() {
        Response result = new Response();
        List<WorkType> workType = memberService.findIndividualWorkTypeList();
        result.setData(workType);
        return result;
    }


    @ApiOperation(value = "员工密码重置", notes = "员工密码重置", response = Response.class)
    @RequestMapping(value = "reset_pwd", method = RequestMethod.POST)
    public Response resetPwd(@ApiParam(value = "员工ids,逗号隔开") @RequestParam String ids) {
        Response result = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Member member = new Member();
            String md5Pwd = new Md5Hash("123456", null, 2).toString();
            member.setPassword(md5Pwd);
            member.setId(id);
            memberService.updateMemInfo(member);
        }

        return result;
    }

    @ApiOperation(value = "人员规模", notes = "人员规模", response = Response.class)
    @RequestMapping(value = "sel_employeeSizeList", method = RequestMethod.GET)
    public Response employeeSizeList() {
        Response result = new Response();
        List<EmployeeSize> employeeSizeList = memberService.findEmployeeSizeList();
        result.setData(employeeSizeList);

        return result;
    }
}
