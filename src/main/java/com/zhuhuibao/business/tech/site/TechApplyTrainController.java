package com.zhuhuibao.business.tech.site;

import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.TechExpertCourseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    @Autowired
    private ExpertService expertService;

    @RequestMapping(value="add_course", method = RequestMethod.POST)
    @ApiOperation(value="申请技术培训课程",notes = "申请技术培训课程",response = Response.class)
    public Response insertTechTrainCourse(@ApiParam(value = "申请技术培训课程")  @ModelAttribute(value="techCourse")TechExpertCourse techCourse)
    {
        Response response = new Response();
        expertService.checkMobileCode(techCourse.getCode(),techCourse.getMobile(), TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        Long createId = ShiroUtil.getCreateID();
        if(createId != null) {
            techCourse.setProposerId(createId);
            techCourseService.insertTechExpertCourse(techCourse);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value="开课申请获取验证码",notes="开课申请获取验证码",response = Response.class)
    @RequestMapping(value = "get_classMobileCode", method = RequestMethod.GET)
    public Response get_classMobileCode(@RequestParam String mobile) throws IOException, ApiException {
        Response response = new Response();
        String verifyCode = expertService.getTrainMobileCode(mobile, TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        return response;
    }
}
