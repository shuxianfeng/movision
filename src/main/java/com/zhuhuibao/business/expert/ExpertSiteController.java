package com.zhuhuibao.business.expert;

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
 * Created by cxx on 2016/5/18 0018.
 */
@RestController
@RequestMapping("/rest/expert")
@Api(value="Expert", description="专家-前台")
public class ExpertSiteController {
    private static final Logger log = LoggerFactory
            .getLogger(ExpertSiteController.class);

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value="技术成果列表(前台)",notes="技术成果列表(前台)",response = JsonResult.class)
    @RequestMapping(value = "achievementList", method = RequestMethod.GET)
    public JsonResult achievementList(@RequestParam(required = false) String systemType,
                                        @RequestParam(required = false)String useArea,
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
        map.put("systemType",systemType);
        map.put("useArea",useArea);
        map.put("type",1);
        List<Achievement> achievementList = expertService.findAllAchievementList(pager,map);
        pager.result(achievementList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    @ApiOperation(value="技术成果列表(运营)",notes="技术成果列表(运营)",response = JsonResult.class)
    @RequestMapping(value = "achievementListOms", method = RequestMethod.GET)
    public JsonResult achievementListOms(@RequestParam(required = false) String title,
                                         @RequestParam(required = false)String status,
                                         @RequestParam(required = false) String systemType,
                                      @RequestParam(required = false)String useArea,
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
        //查询传参+
        map.put("systemType",systemType);
        map.put("useArea",useArea);
        map.put("title",title);
        map.put("status",status);
        List<Achievement> achievementList = expertService.findAllAchievementList(pager,map);
        pager.result(achievementList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    @ApiOperation(value="协会动态列表(前台)",notes="协会动态列表(前台)",response = JsonResult.class)
    @RequestMapping(value = "dynamicList", method = RequestMethod.GET)
    public JsonResult dynamicList(@RequestParam(required = false)String pageNo,
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
        map.put("type",1);
        List<Dynamic> dynamicList = expertService.findAllDynamicList(pager,map);
        pager.result(dynamicList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    @ApiOperation(value="协会动态列表(运营)",notes="协会动态列表(运营)",response = JsonResult.class)
    @RequestMapping(value = "dynamicListOms", method = RequestMethod.GET)
    public JsonResult dynamicListOms(@RequestParam(required = false) String title,
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
        List<Dynamic> dynamicList = expertService.findAllDynamicList(pager,map);
        pager.result(dynamicList);
        jsonResult.setData(pager);
        return jsonResult;
    }
}
