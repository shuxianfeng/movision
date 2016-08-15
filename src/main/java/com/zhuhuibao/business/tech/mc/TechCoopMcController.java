package com.zhuhuibao.business.tech.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.tech.entity.TechCooperation;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的技术合作：技术成果，技术需求
 *
 * @author pl
 * @version 2016/6/8 0008
 */
@RestController
@RequestMapping(value = "/rest/tech/mc/coop")
@Api(value = "TechCoopMc", description = "我的技术合作：技术成果，技术需求")
public class TechCoopMcController {

    @Autowired
    TechCooperationService techService;

    @Autowired
    MemberService memberService;

    @LoginAccess
    @RequestMapping(value = {"sel_tech_cooperation", "cg/sel_tech_cooperation", "xq/sel_tech_cooperation"}, method = RequestMethod.GET)
    @ApiOperation(value = "搜索技术合作(技术成果，技术需求)", notes = "搜索技术合作(技术成果，技术需求)", response = Response.class)
    public Response findAllTechCooperationPager(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                                @ApiParam(value = "类型：1成果，2需求") @RequestParam(required = false) String type,
                                                @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                                @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<>();
        Long createID = ShiroUtil.getCreateID();

        condition.put("createID", createID);
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        if (StringUtil.isNotEmpty(title)) {
            condition.put("title", title.replace("_", "\\_"));
        }
        if (StringUtil.isNotEmpty(type)) {
            condition.put("type", type);
        }
        if (StringUtil.isNotEmpty(status)) {
            condition.put("status", status);
        }

        List<Map<String, String>> techList = techService.findAllTechCooperationPager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value = {"sel_tech_cooperation_detail", "cg/sel_tech_cooperation_detail", "xq/sel_tech_cooperation_detail"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询技术合作(技术成果，技术需求)", notes = "查询技术合作(技术成果，技术需求)", response = Response.class)
    public Response selectTechCooperationById(@ApiParam(value = "技术合作成果、需求ID") @RequestParam String techCoopId) {
        Response response = new Response();
        Map<String, String> techCoop = techService.selectMcCoopDetail(techCoopId);
        response.setData(techCoop);
        return response;
    }

    @LoginAccess
    @RequestMapping(value = {"upd_tech_cooperation", "cg/upd_tech_cooperation", "xq/upd_tech_cooperation"}, method = RequestMethod.POST)
    @ApiOperation(value = "修改技术合作(技术成果，技术需求)", notes = "修改技术合作(技术成果，技术需求)", response = Response.class)
    public Response updateTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")
                                              @ModelAttribute(value = "techCoop") TechCooperation techCoop) throws Exception {
        int result = techService.updateTechCooperation(techCoop);
        if (result != 1) {
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "保存失败");
        }
        return new Response();
    }

    @LoginAccess
    @RequestMapping(value = {"add_tech_cooperation", "cg/add_tech_cooperation", "xq/add_tech_cooperation"}, method = RequestMethod.POST)
    @ApiOperation(value = "新增技术合作(技术成果，技术需求)", notes = "新增技术合作(技术成果，技术需求)", response = Response.class)
    public Response insertTechCooperation(@ApiParam(value = "技术合作：技术成果，技术需求")
                                              @ModelAttribute(value = "techCoop") TechCooperation techCoop) throws Exception {
        Long createId = ShiroUtil.getCreateID();
        techCoop.setCreateID(createId);
        int result = techService.insertTechCooperation(techCoop);
        if (result != 1) {
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "添加失败");
        }
        return new Response();
    }

    @LoginAccess
    @RequestMapping(value = {"del_tech_cooperation", "cg/del_tech_cooperation", "xq/del_tech_cooperation"}, method = RequestMethod.POST)
    @ApiOperation(value = "删除技术合作(技术成果，技术需求)", notes = "删除技术合作(技术成果，技术需求)", response = Response.class)
    public Response deleteTechCooperation(@ApiParam(value = "技术合作ID") @RequestParam() String techId) {
        Map<String, Object> condition = new HashMap<>();
        condition.put("id", techId);
        condition.put("status", TechConstant.TechCooperationnStatus.DELETE.toString());
        int result = techService.deleteTechCooperation(condition);
        if (result != 1) {
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "删除失败");
        }
        return new Response();
    }

    @LoginAccess
    @RequestMapping(value = "cg/sel_my_looked_achievementList", method = RequestMethod.GET)
    @ApiOperation(value = "查询我查看过的技术成果", notes = "查询我查看过的技术成果", response = Response.class)
    public Response sel_my_looked_achievementList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                Integer.valueOf(pageSize));
        Long createId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        Member member = memberService.findMemById(String.valueOf(createId));
        if ("100".equals(member.getWorkType())) {
            map.put("companyId", createId);
        } else {
            map.put("viewerId", createId);
        }
        List<Map<String, String>> achievementList = techService.findAllMyLookedAchievementList(pager, map);
        pager.result(achievementList);
        response.setData(pager);

        return response;
    }

    @LoginAccess
    @RequestMapping(value = "cg/del_batch_my_looked_achievement", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除我查看过的技术成果", notes = "批量删除我查看过的技术成果", response = Response.class)
    public Response del_batch_my_looked_achievement(@RequestParam() String ids) {
        Response response = new Response();
        String idlist[] = ids.split(",");
        for (String id : idlist) {
            techService.deleteLookedAchievement(id);
        }
        return response;
    }
}
