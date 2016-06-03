package com.zhuhuibao.business.tech.site;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.TechExpertCourseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *申请技术培训课程业务处理类
 *@author pl
 *@create 2016/6/1 0001
 **/
@RestController
@RequestMapping("/rest/tech/site/train")
@Api(value = "TechTrainController",description = "申请技术培训课程")
public class TechApplyTrainController {

    @Autowired
    TechExpertCourseService techCourseService;

    @RequestMapping(value="add_course", method = RequestMethod.POST)
    @ApiOperation(value="申请技术培训课程",notes = "申请技术培训课程",response = Response.class)
    public Response insertTechTrainCourse(@ApiParam(value = "申请技术培训课程")  @ModelAttribute(value="techCourse")TechExpertCourse techCourse)
    {
        Long createId = ShiroUtil.getCreateID();
        if(createId != null) {
            techCourse.setProposerId(createId);
            int result = techCourseService.insertTechExpertCourse(techCourse);
        }else{
        throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
    }
        Response response = new Response();
        return response;
    }
}
