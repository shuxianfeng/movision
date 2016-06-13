package com.zhuhuibao.business.tech.site;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.common.constant.TechConstant;
import com.zhuhuibao.common.pojo.OrderReqBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 技术培训课程
 */
@RestController
@RequestMapping("/rest/tech/site/courseOrder")
@Api(value = "techCourse", description = "技术培训购买下单接口")
public class TechCourseController {
    private static final Logger log = LoggerFactory.getLogger(TechCourseController.class);

    private static final String PARTNER = AlipayPropertiesLoader.getPropertyValue("partner");

    @Autowired
    AlipayDirectService alipayDirectService;

    @Autowired
    TechCooperationService techService;

    @Autowired
    private ExpertService expertService;

    @ApiOperation(value = "培训课程下单支付", notes = "培训课程下单支付")
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam @ModelAttribute OrderReqBean order) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(order);

        if ("true".equals(order.getNeedInvoice())) {
            String invoiceTitle = order.getInvoiceTitle();
            if (invoiceTitle == null) {
                log.error("已选需要发票,发票抬头不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "已选需要发票,发票抬头不能为空");
            }
            String invoiceType = order.getInvoiceType();
            if (invoiceType == null) {
                log.error("已选需要发票,发票类型不能为空");
                throw new BusinessException(MsgCodeConstant.PARAMS_VALIDATE_ERROR, "已选需要发票,发票类型不能为空");
            }
        }

        log.info("技术培训下单页面,请求参数:{}", json);
        Map paramMap = gson.fromJson(json, Map.class);

        String buyerId = (String) paramMap.get("buyerId");
        if(StringUtils.isEmpty(buyerId)){
            Long userId = ShiroUtil.getCreateID();
            if (userId == null) {
                log.error("用户未登陆");
                throw new AuthException(MsgCodeConstant.un_login,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }else{
                paramMap.put("buyerId",String.valueOf(userId));
            }
        }

        //特定参数
        paramMap.put("exterInvokeIp", ValidateUtils.getIpAddr(request));//客户端IP地址
        paramMap.put("alipay_goods_type", PayConstants.GoodsType.XNL.toString());//商品类型  0 , 1
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用立即支付接口......");

        //需要判断购买数量是否 >= 产品剩余数量
        alipayDirectService.doPay(response, paramMap);
    }

    @ApiOperation(value="专家培训课程下单获取验证码",notes="专家培训课程下单获取验证码",response = Response.class)
    @RequestMapping(value = "get_mobileCode", method = RequestMethod.GET)
    public Response get_TrainMobileCode(@ApiParam(value = "手机号") @RequestParam String mobile) throws IOException, ApiException {
        Response response = new Response();
        expertService.getTrainMobileCode(mobile, TechConstant.MOBILE_CODE_SESSION_ORDER_CLASS);
        return response;
    }

    @ApiOperation(value="专家培训课程下单验证验证码是否正确",notes="专家培训课程下单验证验证码是否正确",response = Response.class)
    @RequestMapping(value = "check_mobileCode", method = RequestMethod.POST)
    public Response check_mobileCode(@ApiParam(value = "验证码") @RequestParam String code,
                                     @ApiParam(value = "手机号") @RequestParam String mobile)  {
        Response response = new Response();
        expertService.checkMobileCode(code,mobile,TechConstant.MOBILE_CODE_SESSION_ORDER_CLASS);
        return response;
    }

}
