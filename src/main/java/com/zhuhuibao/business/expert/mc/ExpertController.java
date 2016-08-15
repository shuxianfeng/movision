package com.zhuhuibao.business.expert.mc;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.expert.entity.*;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专家会员中心接口管理
 * Created by cxx on 2016/5/17 0017.
 */
@RestController
@RequestMapping("/rest/expert/mc")
@Api(value = "expert", description = "会员中心-专家")
public class ExpertController {
    private static final Logger log = LoggerFactory.getLogger(ExpertController.class);

    @Autowired
    private ExpertService expertService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "我的技术成果(后台)", notes = "我的技术成果(后台)", response = Response.class)
    @RequestMapping(value = "ach/sel_myAchievementList", method = RequestMethod.GET)
    public Response myAchievementList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                      @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                      @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Achievement> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            map.put("createId", String.valueOf(createId));
            List<Achievement> achievementList = expertService.findAllAchievementList(pager, map);
            List list = new ArrayList();
            for (Achievement achievement : achievementList) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", achievement.getId());
                m.put("title", achievement.getTitle());
                m.put("systemName", achievement.getSystemName());
                m.put("useAreaName", achievement.getUseAreaName());
                m.put("updateTime", achievement.getUpdateTime());
                m.put("status", achievement.getStatus());
                list.add(m);
            }
            pager.result(list);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "技术成果详情", notes = "技术成果详情", response = Response.class)
    @RequestMapping(value = "ach/sel_achievement", method = RequestMethod.GET)
    public Response queryAchievementById(@ApiParam(value = "技术成果ID") @RequestParam String id) throws Exception {
        Response response = new Response();
        Map<String, String> map = expertService.queryAchievementById(id);
        if (map != null) {
            response.setData(map);
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }

        return response;
    }

    @ApiOperation(value = "删除技术成果", notes = "删除技术成果", response = Response.class)
    @RequestMapping(value = "ach/del_achievement", method = RequestMethod.POST)
    public Response deleteAchievement(@ApiParam(value = "技术成果ids,逗号隔开") @RequestParam String ids) {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Achievement achievement = new Achievement();
            achievement.setIs_deleted(ExpertConstant.EXPERT_DELETE_ONE);
            achievement.setId(id);
            expertService.updateAchievement(achievement);
        }
        return response;
    }

    @ApiOperation(value = "更新技术成果", notes = "更新技术成果", response = Response.class)
    @RequestMapping(value = "ach/upd_achievement", method = RequestMethod.POST)
    public Response updateAchievement(@ModelAttribute Achievement achievement) {
        Response response = new Response();
        expertService.updateAchievement(achievement);
        return response;
    }

    @ApiOperation(value = "发布协会动态", notes = "发布协会动态", response = Response.class)
    @RequestMapping(value = "dynamic/add_dynamic", method = RequestMethod.POST)
    public Response publishDynamic(@ModelAttribute Dynamic dynamic) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        //判断是否登陆
        if (null != createId) {
            dynamic.setCreaterType("2");
            dynamic.setCreateId(String.valueOf(createId));
            expertService.publishDynamic(dynamic);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "删除协会动态", notes = "删除协会动态", response = Response.class)
    @RequestMapping(value = "dynamic/del_dynamic", method = RequestMethod.POST)
    public Response deleteDynamic(@ApiParam(value = "协会动态ids,逗号隔开") @RequestParam String ids) {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            Dynamic dynamic = new Dynamic();
            dynamic.setIs_deleted(ExpertConstant.EXPERT_DELETE_ONE);
            dynamic.setId(id);
            expertService.updateDynamic(dynamic);
        }
        return response;
    }

    @ApiOperation(value = "更新协会动态", notes = "更新协会动态", response = Response.class)
    @RequestMapping(value = "dynamic/upd_dynamic", method = RequestMethod.POST)
    public Response updateDynamic(@ModelAttribute Dynamic dynamic) {
        Response response = new Response();
        expertService.updateDynamic(dynamic);
        return response;
    }

    @ApiOperation(value = "协会动态详情", notes = "协会动态详情", response = Response.class)
    @RequestMapping(value = "dynamic/sel_dynamic", method = RequestMethod.GET)
    public Response queryDynamicById(@ApiParam(value = "协会动态Id") @RequestParam String id) throws Exception {
        Response response = new Response();
        Dynamic dynamic = expertService.queryDynamicById(id);
        if (dynamic != null) {
            response.setData(dynamic);
        } else {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }
        return response;
    }

    @ApiOperation(value = "我的协会动态(后台)", notes = "我的协会动态(后台)", response = Response.class)
    @RequestMapping(value = "dynamic/sel_myDynamicList", method = RequestMethod.GET)
    public Response myDynamicList(@ApiParam(value = "标题") @RequestParam(required = false) String title,
                                  @ApiParam(value = "状态") @RequestParam(required = false) String status,
                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        //查询传参
        map.put("title", title);
        map.put("status", status);
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            map.put("createId", String.valueOf(createId));
            List<Dynamic> dynamicList = expertService.findAllDynamicList(pager, map);
            List list = new ArrayList();
            for (Dynamic Dynamic : dynamicList) {
                Map m = new HashMap();
                m.put("id", Dynamic.getId());
                m.put("title", Dynamic.getTitle());
                m.put("updateTime", Dynamic.getUpdateTime());
                m.put("status", Dynamic.getStatus());
                list.add(m);
            }
            pager.result(list);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "更新专家信息", notes = "更新专家信息", response = Response.class)
    @RequestMapping(value = "base/upd_expert", method = RequestMethod.POST)
    public Response updateExpert(@ModelAttribute Expert expert) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            expert.setCreateId(String.valueOf(createId));
            expertService.updateExpertByCreateid(expert);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我的专家全部信息", notes = "查询我的专家全部信息", response = Response.class)
    @RequestMapping(value = "base/sel_expert", method = RequestMethod.GET)
    public Response queryExpertById() {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            Expert expert = expertService.queryExpertByCreateId(String.valueOf(createId));
            response.setData(expert);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

    @ApiOperation(value = "查询等我回答的問題列表", notes = "查询专家頁面等我回答的問題列表", response = Response.class)
    @RequestMapping(value = "base/sel_questionList", method = RequestMethod.GET)
    public Response queryExpertQuestion(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            map.put("id", String.valueOf(createId));
            List<Map<String, String>> questionList = expertService.queryExpertQuestion(pager, map);
            pager.result(questionList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "立刻回答", notes = "立刻回答", response = Response.class)
    @RequestMapping(value = "base/add_answer", method = RequestMethod.POST)
    public Response answerQuestion(@ModelAttribute Answer answer) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            answer.setCreateid(String.valueOf(createId));
            answer.setStatus("0");
            expertService.answerQuestion(answer);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我(專家)已經回答的問題列表", notes = "查询我(專家)已經回答的問題列表", response = Response.class)
    @RequestMapping(value = "base/sel_myAnswerList", method = RequestMethod.GET)
    public Response queryMyAnswerQuestion(@RequestParam(required = false,defaultValue = "1") String pageNo,
                                          @RequestParam(required = false,defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            map.put("id", String.valueOf(createId));
            List<Map<String, String>> questionList = expertService.queryMyAnswerQuestion(pager, map);
            pager.result(questionList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我提問的問題列表", notes = "查询我提問的問題列表", response = Response.class)
    @RequestMapping(value = "base/sel_myQuestionList", method = RequestMethod.GET)
    public Response queryMyQuestion(@RequestParam(required = false,defaultValue = "1") String pageNo,
                                    @RequestParam(required = false,defaultValue = "10") String pageSize) {
        Response response = new Response();

        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> map = new HashMap<>();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            map.put("id", String.valueOf(createId));
            List<Map<String, String>> questionList = expertService.queryMyQuestion(pager, map);
            pager.result(questionList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "查询我提問的一條問題及其回答內容", notes = "查询我提問的一條問題及其回答內容", response = Response.class)
    @RequestMapping(value = "base/sel_myQuestion", method = RequestMethod.GET)
    public Response queryMyQuestionById(@ApiParam(value = "問題id") @RequestParam String id) {
        Response response = new Response();
        Map map = expertService.queryMyQuestionById(id);
        response.setData(map);
        return response;
    }

    @ApiOperation(value = "关闭问题", notes = "关闭问题", response = Response.class)
    @RequestMapping(value = "base/upd_closeQuestion", method = RequestMethod.POST)
    public Response closeQuestion(@ApiParam(value = "問題id") @RequestParam String id) {
        Response response = new Response();
        Question question = new Question();
        question.setId(id);
        //狀態設為已關閉
        question.setStatus(ExpertConstant.EXPERT_QUESTION_STATUS_TWO);
        expertService.updateQuestionInfo(question);
        return response;
    }

    @ApiOperation(value = "采纳答案", notes = "采纳答案", response = Response.class)
    @RequestMapping(value = "base/upd_acceptAnswer", method = RequestMethod.POST)
    public Response acceptAnswer(@ApiParam(value = "問題id") @RequestParam String questionId,
                                 @ApiParam(value = "答案id") @RequestParam String answerId) {
        Response response = new Response();
        Question question = new Question();
        question.setId(questionId);
        //設置採納答案id
        question.setAnswerId(answerId);
        //狀態設為已關閉
        question.setStatus(ExpertConstant.EXPERT_QUESTION_STATUS_FOUR);
        expertService.updateQuestionInfo(question);
        return response;
    }

    @RequestMapping(value = "sel_my_looked_achievementList", method = RequestMethod.GET)
    @ApiOperation(value = "查询我查看过的专家技术成果", notes = "查询我查看过的专家技术成果", response = Response.class)
    public Response sel_my_looked_achievementList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                Integer.valueOf(pageSize));
        Long createId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        if (createId != null) {
            Member member = memberService.findMemById(String.valueOf(createId));
            if ("100".equals(member.getWorkType())) {
                map.put("companyId", createId);
            } else {
                map.put("viewerId", createId);
            }
            List<Map<String, String>> achievementList = expertService.findAllMyLookedAchievementList(pager, map);
            pager.result(achievementList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                    .valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value = "del_batch_my_looked_achievement", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除我查看过的专家技术成果", notes = "批量删除我查看过的专家技术成果", response = Response.class)
    public Response del_batch_my_looked_achievement(@RequestParam() String ids) {
        Response response = new Response();
        String idlist[] = ids.split(",");
        for (String id : idlist) {
            expertService.deleteLookedAchievement(id);
        }
        return response;
    }

    @RequestMapping(value = "sel_my_looked_expertList", method = RequestMethod.GET)
    @ApiOperation(value = "查询我查看过的专家列表", notes = "查询我查看过的专家列表", response = Response.class)
    public Response sel_my_looked_expertList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                             @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                Integer.valueOf(pageSize));
        Long createId = ShiroUtil.getCreateID();
        Map<String, Object> map = new HashMap<>();
        if (createId != null) {
            Member member = memberService.findMemById(String.valueOf(createId));
            if ("100".equals(member.getWorkType())) {
                map.put("companyId", createId);
            } else {
                map.put("viewerId", createId);
            }
            List<Map<String, String>> expertList = expertService.findAllMyLookedExpertList(pager, map);
            pager.result(expertList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                    .valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @RequestMapping(value = "del_batch_my_looked_expert", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除我查看过的专家", notes = "批量删除我查看过的专家", response = Response.class)
    public Response del_batch_my_looked_expert(@RequestParam() String ids) {
        Response response = new Response();
        String idlist[] = ids.split(",");
        for (String id : idlist) {
            expertService.deleteLookedExpert(id);
        }
        return response;
    }
}
