package com.zhuhuibao.business.memCenter.ExpertManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
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

    @ApiOperation(value = "我的技术成果(后台)", notes = "我的技术成果(后台)", response = Response.class)
    @RequestMapping(value = "myAchievementList", method = RequestMethod.GET)
    public Response myAchievementList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                      @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                      @RequestParam(required = false) String pageNo,
                                      @RequestParam(required = false) String pageSize) {
        Response response = new Response();
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
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "删除技术成果", notes = "删除技术成果", response = Response.class)
    @RequestMapping(value = "deleteAchievement", method = RequestMethod.POST)
    public Response deleteAchievement(@ApiParam(value = "技术成果ids,逗号隔开") @RequestParam String ids)
            {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Achievement achievement = new Achievement();
            achievement.setIs_deleted(is_deleted);
            achievement.setId(id);
            expertService.updateAchievement(achievement);
        }
        return response;
    }

    @ApiOperation(value = "更新技术成果", notes = "更新技术成果", response = Response.class)
    @RequestMapping(value = "updateAchievement", method = RequestMethod.POST)
    public Response updateAchievement(Achievement achievement)  {
        Response response = new Response();
        expertService.updateAchievement(achievement);
        return response;
    }

    @ApiOperation(value = "发布协会动态", notes = "发布协会动态", response = Response.class)
    @RequestMapping(value = "publishDynamic", method = RequestMethod.POST)
    public Response publishDynamic(Dynamic dynamic)  {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                dynamic.setCreateId(principal.getId().toString());
                expertService.publishDynamic(dynamic);
            } else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "删除协会动态", notes = "删除协会动态", response = Response.class)
    @RequestMapping(value = "deleteDynamic", method = RequestMethod.POST)
    public Response deleteDynamic(@ApiParam(value = "协会动态ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Dynamic dynamic = new Dynamic();
            dynamic.setIs_deleted(is_deleted);
            dynamic.setId(id);
            expertService.updateDynamic(dynamic);
        }
        return response;
    }

    @ApiOperation(value = "更新协会动态", notes = "更新协会动态", response = Response.class)
    @RequestMapping(value = "updateDynamic", method = RequestMethod.POST)
    public Response updateDynamic(Dynamic dynamic)  {
        Response response = new Response();
        expertService.updateDynamic(dynamic);
        return response;
    }

    @ApiOperation(value = "我的协会动态(后台)", notes = "我的协会动态(后台)", response = Response.class)
    @RequestMapping(value = "myDynamicList", method = RequestMethod.GET)
    public Response myDynamicList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                  @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                  @RequestParam(required = false) String pageNo,
                                  @RequestParam(required = false) String pageSize)  {
        Response response = new Response();
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
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        } else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "更新专家信息", notes = "更新专家信息", response = Response.class)
    @RequestMapping(value = "updateExpert", method = RequestMethod.POST)
    public Response updateExpert(Expert expert)  {
        Response response = new Response();
        expertService.updateExpert(expert);
        return response;
    }

    @ApiOperation(value = "根据id查询专家全部信息", notes = "根据id查询专家全部信息", response = Response.class)
    @RequestMapping(value = "queryExpertById", method = RequestMethod.GET)
    public Response queryExpertById(@ApiParam(value = "专家id") @RequestParam String id)  {
        Response response = new Response();
        Expert expert = expertService.queryExpertById(id);
        response.setData(expert);
        return response;
    }
}
