package com.zhuhuibao.business.expert;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Achievement;
import com.zhuhuibao.mybatis.memCenter.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.entity.Expert;
import com.zhuhuibao.mybatis.memCenter.entity.Question;
import com.zhuhuibao.mybatis.memCenter.service.ExpertService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/5/18 0018.
 */
@RestController
@RequestMapping("/rest/expertSite")
public class ExpertSiteController {
    private static final Logger log = LoggerFactory
            .getLogger(ExpertSiteController.class);

    @Autowired
    private ExpertService expertService;


    @ApiOperation(value="发布技术成果",notes="发布技术成果",response = Response.class)
    @RequestMapping(value = "publishAchievement", method = RequestMethod.POST)
    public Response publishAchievement(@ModelAttribute Achievement achievement) throws Exception {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                achievement.setCreateId(principal.getId().toString());
                expertService.publishAchievement(achievement);
            }else{
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="技术成果详情",notes="技术成果详情",response = Response.class)
    @RequestMapping(value = "queryAchievementById", method = RequestMethod.GET)
    public Response queryAchievementById(@ApiParam(value = "技术成果ID")@RequestParam String id) throws Exception {
        Response response = new Response();
        Achievement achievement = expertService.queryAchievementById(id);
        response.setData(achievement);
        return response;
    }

    @ApiOperation(value="技术成果列表(前台分页)",notes="技术成果列表(前台分页)",response = Response.class)
    @RequestMapping(value = "achievementList", method = RequestMethod.GET)
    public Response achievementList(@ApiParam(value = "系统分类")@RequestParam(required = false) String systemType,
                                    @ApiParam(value = "应用领域")@RequestParam(required = false)String useArea,
                                    @RequestParam(required = false)String pageNo,
                                    @RequestParam(required = false)String pageSize) throws Exception {
        Response response = new Response();
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
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="技术成果列表(前台)控制条数",notes="技术成果列表(前台)控制条数",response = Response.class)
    @RequestMapping(value = "achievementListByCount", method = RequestMethod.GET)
    public Response achievementListByCount(@ApiParam(value = "条数")@RequestParam(required = false) int count) throws Exception {
        Response response = new Response();
        List<Map<String,String>> achievementList = expertService.findAchievementListByCount(count);
        response.setData(achievementList);
        return response;
    }

    @ApiOperation(value="协会动态详情",notes="协会动态详情",response = Response.class)
    @RequestMapping(value = "queryDynamicById", method = RequestMethod.GET)
    public Response queryDynamicById(@ApiParam(value = "协会动态Id")@RequestParam String id) throws Exception {
        Response response = new Response();
        Dynamic dynamic = expertService.queryDynamicById(id);
        response.setData(dynamic);
        return response;
    }

    @ApiOperation(value="协会动态列表(前台分页)",notes="协会动态列表(前台分页)",response = Response.class)
    @RequestMapping(value = "dynamicList", method = RequestMethod.GET)
    public Response dynamicList(@RequestParam(required = false)String pageNo,
                                @RequestParam(required = false)String pageSize) throws Exception {
        Response response = new Response();
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
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="协会动态列表(前台)控制条数",notes="协会动态列表(前台)控制条数",response = Response.class)
    @RequestMapping(value = "dynamicListByCount", method = RequestMethod.GET)
    public Response dynamicListByCount(@ApiParam(value = "条数")@RequestParam(required = false) int count) throws Exception {
        Response response = new Response();
        List<Map<String,String>> dynamicList = expertService.findDynamicListByCount(count);
        response.setData(dynamicList);
        return response;
    }

    @ApiOperation(value="申请专家",notes="申请专家",response = Response.class)
    @RequestMapping(value = "applyExpert", method = RequestMethod.POST)
    public Response applyExpert(@ModelAttribute Expert expert) throws Exception {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                expert.setCreateId(principal.getId().toString());
                expertService.applyExpert(expert);
            }else{
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="专家详情(前台)",notes="专家详情(前台)",response = Response.class)
    @RequestMapping(value = "expertInfo", method = RequestMethod.GET)
    public Response expertInfo(@ApiParam(value = "专家id")@RequestParam String id) {
        Response response = new Response();
        Expert expert = expertService.queryExpertById(id);
        //返回到页面
        Map map = new HashMap();
        map.put("id",expert.getId());
        map.put("name",expert.getName());
        map.put("company",expert.getCompany());
        map.put("position",expert.getPosition());
        map.put("title",expert.getTitle());
        map.put("photo",expert.getPhotoUrl());
        map.put("province",expert.getProvinceName());
        map.put("city",expert.getCityName());
        map.put("area",expert.getAreaName());
        map.put("hot",expert.getViews());
        map.put("introduce",expert.getIntroduce());
        //技术成果
        Map<String,Object> achievementMap = new HashMap<>();
        //查询传参
        achievementMap.put("createId",expert.getCreateId());
        achievementMap.put("status",1);
        List<Achievement> achievementList = expertService.findAchievementList(map);
        map.put("achievementList",achievementList);
        response.setData(map);
        //点击率加1
        expert.setViews(expert.getViews()+1);
        expertService.updateExpert(expert);
        return response;
    }

    @ApiOperation(value="专家联系方式详情(前台)",notes="专家联系方式详情(前台)",response = Response.class)
    @RequestMapping(value = "expertContactInfo", method = RequestMethod.GET)
    public Response expertContactInfo(@ApiParam(value = "专家id")@RequestParam String id)  {
        Response response = new Response();
        Expert expert = expertService.queryExpertById(id);
        //返回到页面
        Map map = new HashMap();
        map.put("address",expert.getAddress());
        map.put("telephone",expert.getTelephone());
        map.put("mobile",expert.getMobile());
        response.setData(map);
        return response;
    }

    @ApiOperation(value="专家列表(前台分页)",notes="专家列表(前台分页)",response = Response.class)
    @RequestMapping(value = "expertList", method = RequestMethod.GET)
    public Response expertList(@ApiParam(value = "省")@RequestParam(required = false) String province,
                               @ApiParam(value = "专家类型")@RequestParam(required = false) String expertType,
                               @RequestParam(required = false)String pageNo,
                               @RequestParam(required = false)String pageSize)  {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Expert> pager = new Paging<Expert>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("province",province);
        map.put("expertType",expertType);
        map.put("type",1);
        List<Expert> expertList = expertService.findAllExpertList(pager,map);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            expertMap.put("hot", expert.getViews());
            expertMap.put("introduce", expert.getIntroduce());
            list.add(expertMap);
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="热门专家(前台)",notes="热门专家(前台)",response = Response.class)
    @RequestMapping(value = "queryHotExpert", method = RequestMethod.GET)
    public Response queryHotExpert(@ApiParam(value = "条数")@RequestParam(required = false) int count) {
        Response response = new Response();
        List<Expert> expertList = expertService.queryHotExpert(count);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            list.add(expertMap);
        }
        response.setData(list);
        return response;
    }

    @ApiOperation(value="最新专家(前台)",notes="最新专家(前台)",response = Response.class)
    @RequestMapping(value = "queryLatestExpert", method = RequestMethod.GET)
    public Response queryLatestExpert(@ApiParam(value = "条数")@RequestParam(required = false) int count) {
        Response response = new Response();
        List<Expert> expertList = expertService.queryLatestExpert(count);
        List list = new ArrayList();
        for (Expert expert : expertList) {
            Map expertMap = new HashMap();
            expertMap.put("id", expert.getId());
            expertMap.put("name", expert.getName());
            expertMap.put("company", expert.getCompany());
            expertMap.put("position", expert.getPosition());
            expertMap.put("photo", expert.getPhotoUrl());
            expertMap.put("hot", expert.getViews());
            expertMap.put("introduce", expert.getIntroduce());
            list.add(expertMap);
        }
        response.setData(list);
        return response;
    }

    /**
     * 向专家提问时的图形验证码
     * @param response
     */
    @ApiOperation(value="向专家提问时的图形验证码",notes="向专家提问时的图形验证码")
    @RequestMapping(value = "imgCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) {
        log.debug("获得验证码");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        log.debug("verifyCode == " + verifyCode);
        sess.setAttribute("expert", verifyCode);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            int w = 100;// 定义图片的width
            int h = 40;// 定义图片的height
            VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value="向专家咨询(前台)",notes="向专家咨询(前台)",response = Response.class)
    @RequestMapping(value = "askExpert", method = RequestMethod.POST)
    public Response askExpert(@ApiParam(value = "咨询内容")@RequestParam String content,
                              @ApiParam(value = "验证码")@RequestParam String code)  {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        if(null != sess) {
            String verifyCode = (String) sess.getAttribute("expert");
            if(!code.equals(verifyCode)){
                throw new BusinessException(MsgCodeConstant.validate_error,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
            }else {
                ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)sess.getAttribute("member");
                if(null != principal){
                    Question question = new Question();
                    question.setCreateid(principal.getId().toString());
                    question.setContent(content);
                    expertService.askExpert(question);
                }else {
                    throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                }
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}
