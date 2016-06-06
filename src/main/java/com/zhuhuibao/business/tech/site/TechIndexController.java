package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.tech.entity.TrainPublishCourse;
import com.zhuhuibao.mybatis.tech.service.PublishTCourseService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.mybatis.tech.service.TechDataService;
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
 * 技术频道首页业务接口
 *
 * @author Administrator
 * @version 2016/6/2 0002
 */
@RestController
@RequestMapping(value = "/rest/tech/site/index")
@Api(value = "TechIndexController",description = "技术频道首页业务接口")
public class TechIndexController {

    @Autowired
    ChannelNewsService newsService;

    @Autowired
    TechDataService techDataService;

    @Autowired
    TechCooperationService coopService;

    @Autowired
    PublishTCourseService ptCourseService;

    @RequestMapping(value = "sel_news_views", method = RequestMethod.GET)
    @ApiOperation(value = "查询新技术播报点击排行",notes = "查询新技术播报点击排行",response = Response.class)
    public Response queryTechViewsByChannel() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", 11);
        map.put("sort", 1);
        map.put("status", 1);
        map.put("count", 10);
        Response response = newsService.queryViewsByChannel(map);
        return response;
    }

    @RequestMapping(value = "sel_news", method = RequestMethod.GET)
    @ApiOperation(value = "查询新技术播报",notes = "查询新技术播报",response = Response.class)
    public Response queryNewsByChannelInfo() {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("channelid", 11);
        map.put("sort", 1);
        map.put("status", 1);
        map.put("count", 5);
        List<ChannelNews> newsList = newsService.queryNewsByChannelInfo(map);
        response.setData(newsList);
        return response;
    }

    @RequestMapping(value = "sel_data", method = RequestMethod.GET)
    @ApiOperation(value = "查看技术频道首页解决方案，技术资料，培训资料信息",notes = "查看技术频道首页解决方案，技术资料，培训资料信息",response = Response.class)
    public Response queryIndexTechData(@ApiParam(value = "1解决方案，2技术资料，3培训资料") @RequestParam Integer fCategory) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fCategory", fCategory);
        map.put("status",TechConstant.TechDataStatus.AUDITPASS.toString());
        map.put("count",23);
        Map<String,List<Map<String,String>>> dataList = techDataService.findIndexTechData(map);
        response.setData(dataList);
        return response;
    }

    @RequestMapping(value = "sel_cooperation", method = RequestMethod.GET)
    @ApiOperation(value = "查看技术成果，技术需求信息",notes = "查看技术成果，技术需求信息",response = Response.class)
    public Response queryByChannelInfo(@ApiParam(value = "1:技术成果，2：技术需求") @RequestParam Integer type) {
        Response response = new Response();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", TechConstant.TechCooperationnStatus.AUDITPASS.toString());
        map.put("type", type);
        map.put("count",6);
        Map<String,List<Map<String,String>>> dataList = coopService.findIndexTechCooperation(map);
        response.setData(dataList);
        return response;
    }

    @RequestMapping(value="sel_latest_train_course", method = RequestMethod.POST)
    @ApiOperation(value="查询最新发布的课程(默认5条)",notes = "查询最新发布的课程(默认5条)",response = Response.class)
    public Response findLatestPublishCourse()
    {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("status",TechConstant.PublishCourseStatus.SALING);
        condition.put("courseType",TechConstant.COURSE_TYPE_TECH);
        condition.put("count",TechConstant.INDEX_LATEST_COUNT_FIVE);
        List<Map<String,String>> courseList = ptCourseService.findLatestPublishCourse(condition);
        Response response = new Response();
        response.setData(courseList);
        return response;
    }

}
