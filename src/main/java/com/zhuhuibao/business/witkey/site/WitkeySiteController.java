package com.zhuhuibao.business.witkey.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.exception.PageNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.CooperationConstants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 威客接口
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/witkey/site/")
public class WitkeySiteController {
    private static final Logger log = LoggerFactory.getLogger(WitkeySiteController.class);

    @Autowired
    private CooperationService cooperationService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "发布任务", notes = "发布任务", response = Response.class)
    @RequestMapping(value = "add_witkey", method = RequestMethod.POST)
    public Response publishCooperation(@ApiParam(value = "威客信息") @ModelAttribute(value = "cooperation") Cooperation cooperation) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            cooperation.setCreateId(String.valueOf(createId));
            cooperationService.publishCooperation(cooperation);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "获取联系方式", notes = "获取联系方式", response = Response.class)
    @RequestMapping(value = "sel_connection", method = RequestMethod.GET)
    public Response sel_connection() throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        Map map = new HashMap();
        if (createId != null) {
            Member member = memberService.findMemById(String.valueOf(createId));
            map.put("telephone", member.getFixedTelephone());
            map.put("mobile", member.getFixedMobile());
            map.put("email", member.getEmail());
            if ("2".equals(member.getIdentify())) {
                map.put("companyName", "");
                map.put("name", member.getNickname());
            } else {
                map.put("companyName", member.getEnterpriseName());
                map.put("name", member.getEnterpriseLinkman());
            }
            response.setData(map);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 查询任务列表（分页）
     */
    @ApiOperation(value = "查询任务列表（前台分页）", notes = "查询任务列表（前台分页）", response = Response.class)
    @RequestMapping(value = "sel_witkeyList", method = RequestMethod.GET)
    public Response findAllCooperationByPager
    (@RequestParam(required = false, defaultValue = "1") String pageNo,
     @RequestParam(required = false, defaultValue = "10") String pageSize,
     @ApiParam(value = "合作类型") @RequestParam(required = false) String type,
     @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
     @ApiParam(value = "系统分类") @RequestParam(required = false) String systemType,
     @ApiParam(value = "省") @RequestParam(required = false) String province,
     @ApiParam(value = "关键字") @RequestParam(required = false) String smart,
     @ApiParam(value = "发布类型，1：接任务，2：接服务，3：资质合作") @RequestParam String parentId) {

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Cooperation cooperation = new Cooperation();
        cooperation.setSmart(smart);
        cooperation.setType(type);
        //区分前台跟后台
        cooperation.setDistinction("1");
        cooperation.setCategory(category);
        cooperation.setProvince(province);
        cooperation.setParentId(parentId);
        cooperation.setSystemType(systemType);
        Response Response = new Response();
        List<Map<String, String>> cooperationList = cooperationService.findAllCooperationByPager(pager, cooperation);
        pager.result(cooperationList);
        Response.setData(pager);
        return Response;
    }

    /**
     * 最热合作信息
     */
    @ApiOperation(value = "最热合作信息", notes = "最热合作信息", response = Response.class)
    @RequestMapping(value = "sel_hot_service", method = RequestMethod.GET)
    public Response queryHotService(@ApiParam(value = "条数") @RequestParam int count,
                                    @ApiParam(value = "合作类型：1：任务，2：服务，3：资质合作") @RequestParam String type) {
        Response Response = new Response();
        Map<String, Object> map = new HashMap<>();
        map.put("count", count);
        map.put("type", type);
        map.put("is_deleted", Constants.DeleteMark.NODELETE.toString());
        map.put("status", CooperationConstants.Status.AUDITED.toString());
        List<Map<String, String>> cooperations = cooperationService.queryHotCooperation(map);
        Response.setData(cooperations);
        return Response;
    }

    @ApiOperation(value = "威客信息詳情", notes = "威客信息詳情", response = Cooperation.class)
    @RequestMapping(value = "sel_witkey", method = RequestMethod.GET)
    public Response cooperationInfo(@RequestParam String id) {
        Response response = new Response();
        Map<String, Object> cooperation = cooperationService.queryCooperationInfoById(id);
        if (!"1".equals(cooperation.get("parentId").toString())) {
            Cooperation result = new Cooperation();
            result.setId(cooperation.get("id").toString());
            result.setViews(String.valueOf(Integer.parseInt(cooperation.get("views").toString()) + 1));
            cooperationService.updateCooperationViews(result);
            response.setData(cooperation);
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }
        return response;
    }

    @ApiOperation(value = "未登陆查看威客任务", notes = "未登陆查看威客任务", response = Cooperation.class)
    @RequestMapping(value = "base/sel_witkeyTask", method = RequestMethod.GET)
    public Response unlogineWitkeyInfo(@ApiParam(value = "任务ID") @RequestParam String id) {
        Response response = new Response();
        Map<String, Object> cooperation = cooperationService.queryUnloginCooperationInfo(id);
        response.setData(cooperation);
        return response;
    }

    @RequestMapping(value = "sel_adv_company", method = RequestMethod.GET)
    @ApiOperation(value = "威客广告位", notes = "威客广告位", response = Response.class)
    public Response queryAdvertisingPosition(@ApiParam(value = "频道类型 5:威客") @RequestParam String chanType,
                                             @ApiParam(value = "频道下子页面.task:接任务;service:找服务;cooperation:资质合作") @RequestParam String page,
                                             @ApiParam(value = "广告所在区域:F1:右下角企业广告") @RequestParam String advArea) {
        Response response = new Response();

        Map<String, Object> map = new HashMap<>();
        map.put("chanType", chanType);
        map.put("page", page);
        map.put("advArea", advArea);
        List<Map<String, String>> companyList = memberService.queryCompanyList(map);

        response.setData(companyList);
        return response;
    }
}
