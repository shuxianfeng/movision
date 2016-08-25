package com.zhuhuibao.business.common;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.activity.entity.ActivityApply;
import com.zhuhuibao.mybatis.activity.service.ActivityService;
import com.zhuhuibao.mybatis.memberReg.entity.Validateinfo;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.service.order.ZHOrderService;
import com.zhuhuibao.service.zhpay.ZhpayService;
import com.zhuhuibao.utils.*;
import com.zhuhuibao.utils.sms.SDKSendSms;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
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

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    ActivityService activityService;

    @Autowired
    MemberRegService memberRegService;

    @Autowired
    ZhpayService zhpayService;



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
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
        return new Response();
    }

    @ApiOperation(value="提交报名",notes="提交报名",response = Response.class)
    @RequestMapping(value = "add_activity_apply", method = RequestMethod.POST)
    public Response add_activity_apply(@ModelAttribute ActivityApply activityApply,@RequestParam String mobileCode)  throws IOException, ApiException {
        activityApply.setActivityId("1");
        String orderNo = activityService.applyActivity(activityApply,mobileCode);
        return new Response(orderNo);
    }


    @ApiOperation(value = "立即支付", notes = "立即支付")
    @RequestMapping(value = "do_pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam("订单号") @RequestParam String orderNo,
                      @ApiParam("支付方式 1:支付宝") @RequestParam  String tradeMode,
                      @ApiParam("回调页面") @RequestParam String returnUrl) throws Exception {

        Map paramMap = new HashMap();
        paramMap.put("orderNo",orderNo);
        paramMap.put("tradeMode",tradeMode);

        log.info("活动报名,请求参数:{}", paramMap);

        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("alipay_goods_type", PayConstants.GoodsType.XNL.toString());//商品类型  0 , 1
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号
        //公用回传参数
        Map<String,String> commsMap = new HashMap<>();
        commsMap.put("type", OrderConstants.GoodsType.ACTIVITY_APPLY.toString());
        commsMap.put("url",returnUrl);
        paramMap.put("extra_common_param",JsonUtils.getJsonStringFromMap(commsMap));
        log.debug("调用立即支付接口......");

        zhpayService.doPay(response, paramMap);

    }
}
