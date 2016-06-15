package com.zhuhuibao.business.expo.site;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expo.entity.DistributedOrder;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.mybatis.expo.entity.MeetingOrder;
import com.zhuhuibao.mybatis.expo.service.ExpoService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
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
@RequestMapping("/rest/expo/site/")
public class ExpoSiteController {
    private static final Logger log = LoggerFactory.getLogger(ExpoSiteController.class);

    @Autowired
    private ExpoService exhibitionService;

    /**
     * 发布一站式会展定制
     */
    @ApiOperation(value="发布一站式会展定制",notes="发布一站式会展定制",response = Response.class)
    @RequestMapping(value = "add_meetingOrder", method = RequestMethod.POST)
    public Response publishMeetingOrder(@ModelAttribute()MeetingOrder meetingOrder) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if(createId!=null){
            meetingOrder.setCreateid(String.valueOf(createId));
            String id = exhibitionService.publishMeetingOrder(meetingOrder);
            response.setData(id);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 一站式会展定制查看
     */
    @ApiOperation(value="会展定制查看",notes="会展定制查看",response = Response.class)
    @RequestMapping(value = "sel_meetingOrder", method = RequestMethod.GET)
    public Response queryMeetingOrderInfoById(@RequestParam String id)  {
        Response Response = new Response();
        MeetingOrder meetingOrder = exhibitionService.queryMeetingOrderInfoById(id);
        Response.setData(meetingOrder);
        return Response;
    }

    /**
     * 会展信息列表(前台)
     */
    @ApiOperation(value="会展信息列表(前台)",notes="会展信息列表(前台)",response = Response.class)
    @RequestMapping(value = "sel_expoList", method = RequestMethod.GET)
    public Response findAllExhibition(@ApiParam(value = "所属栏目:1:筑慧活动;2:行业会议;3:厂商活动")@RequestParam(required = false)String type,
                                      @ApiParam(value = "筑慧活动子栏目:1:技术研讨会;2:产品发布会;3:行业峰会;4:市场活动会")@RequestParam(required = false)String subType,
                                      @ApiParam(value = "省")@RequestParam(required = false)String province,
                                      @RequestParam(required = false)String pageNo, @RequestParam(required = false)String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("subType",subType);
        map.put("type",type);
        map.put("province",province);
        map.put("type1",1);
        //查询
        List<Map<String,String>> exhibitionList = exhibitionService.findAllExhibition(pager,map);
        pager.result(exhibitionList);
        response.setData(pager);
        return response;
    }

    /**
     * 最新会展信息
     */
    @ApiOperation(value="最新会展信息",notes="最新会展信息",response = Response.class)
    @RequestMapping(value = "sel_new_expo", method = RequestMethod.GET)
    public Response newExhibition(@ApiParam(value = "条数")@RequestParam int count,
                                  @ApiParam(value = "会展类型：1:筑慧活动;2:行业会议;3:厂商活动")@RequestParam String type
    ){
        Response response = new Response();
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        map.put("type",type);
        List<Map<String,String>> exhibitionList = exhibitionService.findNewExhibition(map);
        response.setData(exhibitionList);
        return response;
    }

    /**
     * 发布分布式会展定制
     */
    @ApiOperation(value="发布分布式会展定制",notes="发布分布式会展定制",response = Response.class)
    @RequestMapping(value = "add_distributedOrder", method = RequestMethod.POST)
    public Response publishDistributedOrder(@ModelAttribute()DistributedOrder distributedOrder) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if(createId!=null){
            distributedOrder.setCreateid(String.valueOf(createId));
            exhibitionService.publishDistributedOrder(distributedOrder);
        }else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }

    /**
     * 会展详情查看
     */
    @ApiOperation(value="会展详情查看",notes="会展详情查看",response = Response.class)
    @RequestMapping(value = "sel_expo", method = RequestMethod.GET)
    public Response exhibitionInfo(@RequestParam String id)  {
        Response response = new Response();
        Exhibition exhibition = exhibitionService.queryExhibitionInfoById(id);
        response.setData(exhibition);
        //更新點擊率
        exhibition.setViews(String.valueOf(Integer.parseInt(exhibition.getViews())+1));
        exhibitionService.updateExhibitionViews(exhibition);
        return response;
    }
}
