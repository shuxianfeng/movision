package com.zhuhuibao.business.tech.site;

import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.tech.entity.TechExpertCourse;
import com.zhuhuibao.mybatis.tech.service.TechExpertCourseService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    @ApiOperation(value="开课申请获取手机验证码",notes="开课申请获取手机验证码",response = Response.class)
    @RequestMapping(value = "sel_classMobileCode", method = RequestMethod.GET)
    public Response get_classMobileCode(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                        @ApiParam(value ="图形验证码") @RequestParam String imgCode) throws IOException, ApiException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            expertService.getTrainMobileCode(mobile, TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return response;
    }

    @ApiOperation(value="申请开课图形验证码",notes="申请开课的图形验证码",response = Response.class)
    @RequestMapping(value = "sel_imgCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute(TechConstant.MOBILE_CODE_SESSION_TYPE_CLASS, verifyCode);
    }
}
