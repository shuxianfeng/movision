package com.zhuhuibao.business.exhibition;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Exhibition;
import com.zhuhuibao.mybatis.memCenter.entity.MeetingOrder;
import com.zhuhuibao.mybatis.memCenter.service.ExhibitionService;
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
 * 会展接口类
 * Created by cxx on 2016/5/11 0011.
 */
@RestController
@RequestMapping("/rest/exhibitionSite")
public class ExhibitionSiteController {
    private static final Logger log = LoggerFactory.getLogger(ExhibitionSiteController.class);

    @Autowired
    private ExhibitionService exhibitionService;

    /**
     * 发布会展定制
     */
    @ApiOperation(value="发布会展定制",notes="发布会展定制",response = JsonResult.class)
    @RequestMapping(value = "publishMeetingOrder", method = RequestMethod.POST)
    public JsonResult publishMeetingOrder(@ModelAttribute()MeetingOrder meetingOrder) {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                meetingOrder.setCreateid(principal.getId().toString());
                exhibitionService.publishMeetingOrder(meetingOrder);
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    /**
     * 会展信息列表(前台)
     */
    @ApiOperation(value="会展信息列表(前台)",notes="会展信息列表(前台)",response = JsonResult.class)
    @RequestMapping(value = "findAllExhibition", method = RequestMethod.GET)
    public JsonResult findAllExhibition(@ApiParam(value = "所属栏目:1:筑慧活动;2:行业会议;3:厂商活动")@RequestParam(required = false)String type,
                                           @ApiParam(value = "筑慧活动子栏目:1:技术研讨会;2:产品发布会;3:行业峰会;4:市场活动会")@RequestParam(required = false)String subType,
                                           @ApiParam(value = "省")@RequestParam(required = false)String province,
                                           @RequestParam(required = false)String pageNo,@RequestParam(required = false)String pageSize) {
        JsonResult jsonResult = new JsonResult();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Exhibition> pager = new Paging<Exhibition>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("subType",subType);
        map.put("type",type);
        map.put("province",province);
        map.put("type1",1);
        //查询
        List<Exhibition> exhibitionList = exhibitionService.findAllExhibition(pager,map);
        pager.result(exhibitionList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    /**
     * 会展信息列表(前台)
     */
    @ApiOperation(value="最新会展信息",notes="最新会展信息",response = JsonResult.class)
    @RequestMapping(value = "newExhibition", method = RequestMethod.GET)
    public JsonResult newExhibition(@ApiParam(value = "条数")@RequestParam int count,
                                    @ApiParam(value = "会展类型：1:筑慧活动;2:行业会议;3:厂商活动")@RequestParam String type
    ){
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        map.put("type",type);
        List<Exhibition> exhibitionList = exhibitionService.findNewExhibition(map);
        jsonResult.setData(exhibitionList);
        return jsonResult;
    }
}
