package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.mybatis.tech.entity.TrainPublishCourse;
import com.zhuhuibao.mybatis.tech.service.PublishTCourseService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 技术频道培训课程业务类
 *
 * @author Administrator
 * @version 2016/6/2 0002
 */
@RestController
@RequestMapping("/rest/tech/site/publish")
@Api(value = "trainCourse",description = "技术频道培训课程业务类")
public class TrainCourseController {

    @Autowired
    PublishTCourseService ptCourseService;

    @RequestMapping(value="sel_publish_course", method = RequestMethod.GET)
    @ApiOperation(value="技术频道搜索培训课程",notes = "技术频道搜索培训课程",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "省") @RequestParam(required = false) String province,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("province",province);
        condition.put("type", TechConstant.COURSE_TYPE_TECH);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        //销售中
        condition.put("status", TechConstant.PublishCourseStatus.SALING.toString());
        List<Map<String, String>> techList = ptCourseService.findAllPublishCoursePager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

    @RequestMapping(value="sel_latest_publish_course", method = RequestMethod.GET)
    @ApiOperation(value="查询最新发布的课程(默认3条)",notes = "查询最新发布的课程(默认3条)",response = Response.class)
    public Response findLatestPublishCourse()
    {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("status",TechConstant.PublishCourseStatus.SALING);
        condition.put("courseType",TechConstant.COURSE_TYPE_TECH);
        condition.put("count",TechConstant.COURSE_LATEST_COUNT_THREE);
        List<Map<String,String>> courseList = ptCourseService.findLatestPublishCourse(condition);
        Response response = new Response();
        response.setData(courseList);
        return response;
    }

    @RequestMapping(value="sel_publish_course_detail", method = RequestMethod.GET)
    @ApiOperation(value="预览课程详情",notes = "预览课程详情",response = Response.class)
    public Response previewPublishCourseDetail(@ApiParam(value = "培训课程ID")  @RequestParam Long courseId)
    {
        Map<String,Object> condition = new HashMap<String,Object>();
        condition.put("courseid",courseId);
        List<Map<String,String>> courseList = ptCourseService.previewTrainCourseDetail(condition);
        Response response = new Response();
        response.setData(courseList);
        return response;
    }
}
