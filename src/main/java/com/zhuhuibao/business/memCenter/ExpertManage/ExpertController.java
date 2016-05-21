package com.zhuhuibao.business.memCenter.ExpertManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.entity.Expert;
import com.zhuhuibao.mybatis.memCenter.service.ExpertService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家会员中心接口管理
 * Created by cxx on 2016/5/17 0017.
 */
@RestController
@RequestMapping("/rest/expert")
@Api(value = "expert", description = "会员中心-专家")
public class ExpertController {
    private static final Logger log = LoggerFactory.getLogger(ExpertController.class);

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value = "我的技术成果(后台)", notes = "我的技术成果(后台)", response = JsonResult.class)
    @RequestMapping(value = "myAchievementList", method = RequestMethod.GET)
    public JsonResult myAchievementList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                        @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String pageNo,
                                        @RequestParam(required = false) String pageSize) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Achievement> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("createId", principal.getId());
                List<Achievement> achievementList = expertService.findAllAchievementList(pager, map);
                pager.result(achievementList);
                jsonResult.setData(pager);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    @ApiOperation(value = "删除技术成果", notes = "删除技术成果", response = JsonResult.class)
    @RequestMapping(value = "deleteAchievement", method = RequestMethod.POST)
    public JsonResult deleteAchievement(@ApiParam(value = "技术成果ids,逗号隔开") @RequestParam String ids)
            throws Exception {
        JsonResult jsonResult = new JsonResult();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Achievement achievement = new Achievement();
            achievement.setIs_deleted(is_deleted);
            achievement.setId(id);
            expertService.updateAchievement(achievement);
        }
        return jsonResult;
    }

    @ApiOperation(value = "更新技术成果", notes = "更新技术成果", response = JsonResult.class)
    @RequestMapping(value = "updateAchievement", method = RequestMethod.POST)
    public JsonResult updateAchievement(Achievement achievement) throws Exception {
        JsonResult jsonResult = new JsonResult();
        expertService.updateAchievement(achievement);
        return jsonResult;
    }

    @ApiOperation(value = "发布协会动态", notes = "发布协会动态", response = JsonResult.class)
    @RequestMapping(value = "publishDynamic", method = RequestMethod.POST)
    public JsonResult publishDynamic(Dynamic dynamic) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                dynamic.setCreateId(principal.getId().toString());
                expertService.publishDynamic(dynamic);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    @ApiOperation(value = "删除协会动态", notes = "删除协会动态", response = JsonResult.class)
    @RequestMapping(value = "deleteDynamic", method = RequestMethod.POST)
    public JsonResult deleteDynamic(@ApiParam(value = "协会动态ids,逗号隔开") @RequestParam String ids) throws Exception {
        JsonResult jsonResult = new JsonResult();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Dynamic dynamic = new Dynamic();
            dynamic.setIs_deleted(is_deleted);
            dynamic.setId(id);
            expertService.updateDynamic(dynamic);
        }
        return jsonResult;
    }

    @ApiOperation(value = "更新协会动态", notes = "更新协会动态", response = JsonResult.class)
    @RequestMapping(value = "updateDynamic", method = RequestMethod.POST)
    public JsonResult updateDynamic(Dynamic dynamic) throws Exception {
        JsonResult jsonResult = new JsonResult();
        expertService.updateDynamic(dynamic);
        return jsonResult;
    }

    @ApiOperation(value = "我的协会动态(后台)", notes = "我的协会动态(后台)", response = JsonResult.class)
    @RequestMapping(value = "myDynamicList", method = RequestMethod.GET)
    public JsonResult myDynamicList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                    @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                    @RequestParam(required = false) String pageNo,
                                    @RequestParam(required = false) String pageSize) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("createId", principal.getId());
                List<Dynamic> dynamicList = expertService.findAllDynamicList(pager, map);
                pager.result(dynamicList);
                jsonResult.setData(pager);
            } else {
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        } else {
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    @ApiOperation(value = "更新专家信息", notes = "更新专家信息", response = JsonResult.class)
    @RequestMapping(value = "updateExpert", method = RequestMethod.POST)
    public JsonResult updateExpert(Expert expert) throws Exception {
        JsonResult jsonResult = new JsonResult();
        expertService.updateExpert(expert);
        return jsonResult;
    }

    @ApiOperation(value = "根据id查询专家全部信息", notes = "根据id查询专家全部信息", response = JsonResult.class)
    @RequestMapping(value = "queryExpertById", method = RequestMethod.GET)
    public JsonResult queryExpertById(@ApiParam(value = "专家id") @RequestParam String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Expert expert = expertService.queryExpertById(id);
        jsonResult.setData(expert);
        return jsonResult;
    }
}
