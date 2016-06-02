package com.zhuhuibao.business.tech.oms;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.TechExpertCourseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *申请技术培训课程业务处理类
 *@author pl
 *@create 2016/6/1 0001
 **/
@RestController
@RequestMapping("/rest/tech/oms/train")
@Api(value = "TechOmsTrainController",description = "申请技术培训课程")
public class TechOmsTrainController {

    @Autowired
    TechExpertCourseService techCourseService;


    @RequestMapping(value="upd_course", method = RequestMethod.POST)
    @ApiOperation(value="技术培训课程",notes = "插入技术培训课程",response = Response.class)
    public Response updateTechTrainCourse(@ApiParam(value = "状态")  @RequestParam int status,
                                          @ApiParam(value = "培训课程ID")  @RequestParam Long techCourseId)
    {
        Long omsOperateId = ShiroUtil.getOmsCreateID();
        if(omsOperateId != null){
            int result = techCourseService.updateTechExpertCourse(techCourseId,status,omsOperateId);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        Response response = new Response();
        return response;
    }

    @RequestMapping(value="sel_course_detail", method = RequestMethod.POST)
    @ApiOperation(value="技术培训课程",notes = "插入技术培训课程",response = Response.class)
    public Response previewTrainCourseDetail(@ApiParam(value = "培训课程ID")  @RequestParam Long techCourseId)
    {
        TechExpertCourse techExpertCourse = techCourseService.selectTechExpertCourseInfo(techCourseId);
        Response response = new Response();
        response.setData(techExpertCourse);
        return response;
    }

    @RequestMapping(value="sel_course_data", method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台搜索技术的申请开课",notes = "运营管理平台搜索申请开课",response = Response.class)
    public Response findAllTechDataPager(@ApiParam(value = "手机号") @RequestParam(required = false) String mobile,
                                         @ApiParam(value = "联系人") @RequestParam(required = false) String linkman,
                                         @ApiParam(value = "状态：1待处理，2已处理") @RequestParam(required = false) String status,
                                         @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                         @ApiParam(value = "每页显示的数目") @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("type", TechConstant.COURSE_TYPE_TECH);
        condition.put("mobile", mobile);
        condition.put("linkman", linkman);
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        condition.put("status", status);
        List<Map<String, String>> techList = techCourseService.findAllOMSTECoursePager(pager, condition);
        pager.result(techList);
        response.setData(pager);
        return response;
    }

}
