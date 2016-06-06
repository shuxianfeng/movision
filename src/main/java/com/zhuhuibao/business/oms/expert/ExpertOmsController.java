package com.zhuhuibao.business.oms.expert;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.*;
import com.zhuhuibao.mybatis.memCenter.service.ExpertService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.shiro.realm.OMSRealm;
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
 * Created by cxx on 2016/5/19 0019.
 */
@RestController
@RequestMapping("/rest/expert/oms")
@Api(value="ExpertOms")
public class ExpertOmsController {
    private static final Logger log = LoggerFactory.getLogger(ExpertOmsController.class);

    @Autowired
    private ExpertService expertService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value="技术成果列表(运营分页)",notes="技术成果列表(运营分页)",response = Response.class)
    @RequestMapping(value = "sel_achievementList", method = RequestMethod.GET)
    public Response achievementListOms(@ApiParam(value = "标题")@RequestParam(required = false) String title,
                                       @ApiParam(value = "状态")@RequestParam(required = false)String status,
                                       @ApiParam(value = "系统分类")@RequestParam(required = false) String systemType,
                                       @ApiParam(value = "应用领域")@RequestParam(required = false)String useArea,
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
        Paging<Achievement> pager = new Paging<Achievement>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参+
        map.put("systemType",systemType);
        map.put("useArea",useArea);
        map.put("title",title);
        map.put("status",status);
        List<Achievement> achievementList = expertService.findAllAchievementList(pager,map);
        pager.result(achievementList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="协会动态列表(运营分页)",notes="协会动态列表(运营分页)",response = Response.class)
    @RequestMapping(value = "sel_dynamicList", method = RequestMethod.GET)
    public Response dynamicListOms(@RequestParam(required = false) String title,
                                   @RequestParam(required = false)String status,
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
        Paging<Dynamic> pager = new Paging<Dynamic>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("title",title);
        map.put("status",status);
        List<Dynamic> dynamicList = expertService.findAllDynamicList(pager,map);
        pager.result(dynamicList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="专家列表(运营分页)",notes="专家列表(运营分页)",response = Response.class)
    @RequestMapping(value = "sel_expertList", method = RequestMethod.GET)
    public Response expertListOms(@ApiParam(value = "姓名")@RequestParam(required = false) String name,
                                  @ApiParam(value = "专家类型")@RequestParam(required = false) String expertType,
                                  @ApiParam(value = "状态")@RequestParam(required = false) String status,
                                  @RequestParam(required = false)String pageNo,
                                  @RequestParam(required = false)String pageSize) {
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
        map.put("name",name);
        map.put("expertType",expertType);
        map.put("status",status);
        List<Expert> expertList = expertService.findAllExpertList(pager,map);
        pager.result(expertList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="专家回答列表",notes="专家回答列表",response = Response.class)
    @RequestMapping(value = "sel_answerList", method = RequestMethod.GET)
    public Response expertAnswerListOms(@RequestParam(required = false)String pageNo,
                                  @RequestParam(required = false)String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String,String>> expertAnswerList = expertService.findAllExpertAnswerListOms(pager);
        pager.result(expertAnswerList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="屏蔽专家回答",notes="屏蔽专家回答",response = Response.class)
    @RequestMapping(value = "refuse_answer", method = RequestMethod.POST)
    public Response refuseExpertAnswer(@RequestParam String id) {
        Response response = new Response();
        Answer answer = new Answer();
        answer.setId(id);
        answer.setStatus(ExpertConstant.EXPERT_ANSWER_STATUS_ONE);
        expertService.updateAnswerInfo(answer);
        return response;
    }

    @ApiOperation(value="屏蔽问题",notes="屏蔽问题",response = Response.class)
    @RequestMapping(value = "refuse_question", method = RequestMethod.POST)
    public Response refuseQuestion(@RequestParam String id) {
        Response response = new Response();
        Question question = new Question();
        question.setId(id);
        question.setStatus(ExpertConstant.EXPERT_QUESTION_STATUS_THREE);
        expertService.updateQuestionInfo(question);
        return response;
    }

    @ApiOperation(value="用户提问列表",notes="用户提问列表",response = Response.class)
    @RequestMapping(value = "sel_questionList", method = RequestMethod.GET)
    public Response questionListOms(@RequestParam(required = false)String pageNo,
                                        @RequestParam(required = false)String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String,String>> questionList = expertService.findAllQuestionListOms(pager);
        pager.result(questionList);
        response.setData(pager);
        return response;
    }


    @ApiOperation(value="专家支持申请列表",notes="专家支持申请列表",response = Response.class)
    @RequestMapping(value = "sel_supportList", method = RequestMethod.GET)
    public Response expertSupportListOms(@RequestParam(required = false)String account,
                                         @RequestParam(required = false)String linkName,
                                         @RequestParam(required = false)String status,
            @RequestParam(required = false)String pageNo,
                                    @RequestParam(required = false)String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Map<String,Object> map = new HashMap<>();
        map.put("account",account);
        map.put("linkName",linkName);
        map.put("status",status);
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<Map<String,String>> expertSupportList = expertService.findAllExpertSupportListOms(pager,map);
        pager.result(expertSupportList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value="专家支持申请处理",notes="专家支持申请处理",response = Response.class)
    @RequestMapping(value = "upd_support", method = RequestMethod.POST)
    public Response updateExpertSupport(@RequestParam String id,@RequestParam String status) {
        Response response = new Response();
        ExpertSupport expertSupport = new ExpertSupport();
        expertSupport.setId(id);
        expertSupport.setStatus(status);
        Long createId = ShiroUtil.getOmsCreateID();
        expertSupport.setUpdateManId(createId.toString());
        expertService.updateExpertSupport(expertSupport);
        return response;
    }

    @ApiOperation(value="查看一条专家支持申请信息",notes="查看一条专家支持申请信息",response = Response.class)
    @RequestMapping(value = "sel_support", method = RequestMethod.GET)
    public Response queryExpertSupportInfoById(@RequestParam String id) {
        Response response = new Response();
        Map<String,String>  map = expertService.queryExpertSupportInfoById(id);
        response.setData(map);
        return response;
    }

}
