package com.zhuhuibao.business.memCenter.ExpertManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.service.ExpertService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家会员中心接口管理
 * Created by cxx on 2016/5/17 0017.
 */
@RestController
@RequestMapping("/rest/expert")
@Api(value="Expert", description="专家-会员中心")
public class ExpertController {
    private static final Logger log = LoggerFactory
            .getLogger(ExpertController.class);

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value="发布技术成果",notes="发布技术成果",response = JsonResult.class)
    @RequestMapping(value = "publishAchievement", method = RequestMethod.POST)
    public JsonResult publishAchievement(Achievement achievement) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                achievement.setCreateId(principal.getId().toString());
                expertService.publishAchievement(achievement);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    @ApiOperation(value="技术成果详情",notes="技术成果详情",response = JsonResult.class)
    @RequestMapping(value = "queryAchievementById", method = RequestMethod.GET)
    public JsonResult queryAchievementById(@RequestParam String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Achievement achievement = expertService.queryAchievementById(id);
        jsonResult.setData(achievement);
        return jsonResult;
    }

    @ApiOperation(value="我的技术成果(后台)",notes="我的技术成果(后台)",response = JsonResult.class)
    @RequestMapping(value = "myAchievementList", method = RequestMethod.GET)
    public JsonResult myAchievementList(@RequestParam(required = false) String title,
                                        @RequestParam(required = false)String status,
                                        @RequestParam(required = false)String pageNo,
                                        @RequestParam(required = false)String pageSize) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Achievement> pager = new Paging<Achievement>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("title",title);
        map.put("status",status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                map.put("createid",principal.getId());
                List<Achievement> achievementList = expertService.findAllAchievementList(pager,map);
                pager.result(achievementList);
                jsonResult.setData(pager);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    @ApiOperation(value="删除成果详情",notes="删除成果详情",response = JsonResult.class)
    @RequestMapping(value = "deleteAchievement", method = RequestMethod.POST)
    public JsonResult deleteAchievement(@RequestParam String ids[]) throws Exception {
        JsonResult jsonResult = new JsonResult();
        for(int i=0;i<ids.length;i++){
            String id = ids[i];
            String is_deleted = "1";
            Achievement achievement = new Achievement();
            achievement.setIs_deleted(is_deleted);
            achievement.setId(id);
            expertService.updateAchievement(achievement);
        }
        return jsonResult;
    }

    @ApiOperation(value="更新成果详情",notes="更新成果详情",response = JsonResult.class)
    @RequestMapping(value = "updateAchievement", method = RequestMethod.POST)
    public JsonResult updateAchievement(Achievement achievement) throws Exception {
        JsonResult jsonResult = new JsonResult();
        expertService.updateAchievement(achievement);
        return jsonResult;
    }

    @ApiOperation(value="发布协会动态",notes="发布协会动态",response = JsonResult.class)
    @RequestMapping(value = "publishDynamic", method = RequestMethod.POST)
    public JsonResult publishDynamic(Dynamic dynamic) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                dynamic.setCreateId(principal.getId().toString());
                expertService.publishDynamic(dynamic);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    @ApiOperation(value="协会动态详情",notes="协会动态详情",response = JsonResult.class)
    @RequestMapping(value = "queryDynamicById", method = RequestMethod.GET)
    public JsonResult queryDynamicById(@RequestParam String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Dynamic dynamic = expertService.queryDynamicById(id);
        jsonResult.setData(dynamic);
        return jsonResult;
    }

    @ApiOperation(value="删除协会动态",notes="删除协会动态",response = JsonResult.class)
    @RequestMapping(value = "deleteDynamic", method = RequestMethod.POST)
    public JsonResult deleteDynamic(@RequestParam String ids[]) throws Exception {
        JsonResult jsonResult = new JsonResult();
        for(int i=0;i<ids.length;i++){
            String id = ids[i];
            String is_deleted = "1";
            Dynamic dynamic = new Dynamic();
            dynamic.setIs_deleted(is_deleted);
            dynamic.setId(id);
            expertService.updateDynamic(dynamic);
        }
        return jsonResult;
    }

    @ApiOperation(value="更新协会动态",notes="更新协会动态",response = JsonResult.class)
    @RequestMapping(value = "updateDynamic", method = RequestMethod.POST)
    public JsonResult updateDynamic(Dynamic dynamic) throws Exception {
        JsonResult jsonResult = new JsonResult();
        expertService.updateDynamic(dynamic);
        return jsonResult;
    }

    @ApiOperation(value="我的协会动态(后台)",notes="我的协会动态(后台)",response = JsonResult.class)
    @RequestMapping(value = "myDynamicList", method = RequestMethod.GET)
    public JsonResult myDynamicList(@RequestParam(required = false) String title,
                                        @RequestParam(required = false)String status,
                                        @RequestParam(required = false)String pageNo,
                                        @RequestParam(required = false)String pageSize) throws Exception {
        JsonResult jsonResult = new JsonResult();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Dynamic> pager = new Paging<Dynamic>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("title",title);
        map.put("status",status);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                map.put("createid",principal.getId());
                List<Dynamic> dynamicList = expertService.findAllDynamicList(pager,map);
                pager.result(dynamicList);
                jsonResult.setData(pager);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }
}
