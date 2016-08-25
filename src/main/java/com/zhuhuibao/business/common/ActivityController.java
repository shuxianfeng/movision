package com.zhuhuibao.business.common;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.activity.entity.ActivityApply;
import com.zhuhuibao.mybatis.activity.service.ActivityService;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.PropertiesUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
import com.zhuhuibao.utils.sms.SDKSendSms;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jianglz
 * @since 2016/8/25.
 */
@RestController
@RequestMapping("/rest/activity")
public class ActivityController {
    private static final Logger log = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    ActivityService activityService;

    @Autowired
    MemberRegService memberRegService;

    @ApiOperation(value="图形验证码",notes="图形验证码")
    @RequestMapping(value = "get_imgCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) {
        log.debug("获得验证码");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control",
                "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        // 生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
        sess.setAttribute("activity", verifyCode);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            int w = 100;// 定义图片的width
            int h = 40;// 定义图片的height
            VerifyCodeUtils.outputImage1(w, h, out, verifyCode);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ApiOperation(value="获取验证码",notes="获取验证码",response = Response.class)
    @RequestMapping(value = "get_mobileCode", method = RequestMethod.GET)
    public Response get_mobileCode(@ApiParam(value = "手机号码") @RequestParam String mobile,
                                   @ApiParam(value ="图形验证码") @RequestParam String imgCode)  throws IOException, ApiException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute("activity");
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            String verifyCode = VerifyCodeUtils.generateVerifyCode(Constants.CHECK_MOBILE_CODE_SIZE,
                    VerifyCodeUtils.VERIFY_CODES_DIGIT);
            log.debug("verifyCode == " + verifyCode);
            // 发送验证码到手机
            Map<String, String> map = new LinkedHashMap<>();
            map.put("code", verifyCode);
            map.put("time", Constants.sms_time);
            Gson gson = new Gson();
            String params = gson.toJson(map);
            SDKSendSms.sendSMS(mobile, params, PropertiesUtils.getValue("zhuhuibao_check_mobile_template_code"));

            Validateinfo info = new Validateinfo();
            info.setCreateTime(DateUtils.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            info.setCheckCode(verifyCode);
            info.setAccount(mobile);
            memberRegService.inserValidateInfo(info);
            sess.setAttribute("activity"+mobile, verifyCode);
            response.setData(verifyCode);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return response;
    }

    @ApiOperation(value="提交报名",notes="提交报名",response = Response.class)
    @RequestMapping(value = "add_activity_apply", method = RequestMethod.POST)
    public Response add_activity_apply(@ModelAttribute ActivityApply activityApply,@RequestParam String mobileCode)  throws IOException, ApiException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessMobileCode = (String) sess.getAttribute("activity"+activityApply.getApplyMobile());
        if(mobileCode.equals(sessMobileCode)){
            activityApply.setActivityId("1");
            activityService.addActivity(activityApply);
        }else{
            throw new BusinessException(MsgCodeConstant.member_mcode_mobile_validate_error,
                    MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.member_mcode_mobile_validate_error)));
        }
        return response;
    }
}
