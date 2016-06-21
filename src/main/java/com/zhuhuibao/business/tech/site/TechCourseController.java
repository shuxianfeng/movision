package com.zhuhuibao.business.tech.site;

import com.google.gson.Gson;
import com.taobao.api.ApiException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.alipay.service.direct.AlipayDirectService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.*;
import com.zhuhuibao.common.pojo.OrderReqBean;
import com.zhuhuibao.common.pojo.PayReqBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.expert.service.ExpertService;
import com.zhuhuibao.mybatis.order.entity.InvoiceRecord;
import com.zhuhuibao.mybatis.order.service.InvoiceService;
import com.zhuhuibao.mybatis.tech.service.OrderManagerService;
import com.zhuhuibao.mybatis.tech.service.TechCooperationService;
import com.zhuhuibao.service.course.CourseService;
import com.zhuhuibao.service.zhpay.ZhpayService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.ValidateUtils;
import com.zhuhuibao.utils.VerifyCodeUtils;
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
    CourseService courseService;

    @Autowired
    ZhpayService zhpayService;

    @Autowired
    ExpertService expertService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    OrderManagerService orderManagerService;

    @ApiOperation(value = "培训课程下单", notes = "培训课程下单")
    @RequestMapping(value = "order", method = RequestMethod.POST)
    public void createOrder(@ApiParam @ModelAttribute OrderReqBean order){
        Gson gson = new Gson();
        String json = gson.toJson(order);

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
        paramMap.put("partner", PARTNER);//partner=seller_id     商家支付宝ID  合作伙伴身份ID 签约账号

        log.debug("调用下单接口......");

        courseService.createOrder(paramMap);

    }


    @ApiOperation(value = "培训课程支付", notes = "培训课程支付")
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void doPay(HttpServletRequest request, HttpServletResponse response,
                      @ApiParam @ModelAttribute PayReqBean pay) throws Exception {

        Gson gson = new Gson();
        String json = gson.toJson(pay);


        log.info("技术培训支付页面,请求参数:{}", json);
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

        //判断支付方式   是否使用筑慧币
        String userZHB = pay.getUserZHB();

        switch(userZHB){
            case "true":
                zhpayService.doPayMultiple(response,paramMap);
                break;
            case "false":
                zhpayService.doPay(response,paramMap);
                break;
            default:
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"是否使用筑慧币,传参错误");
        }

    }

    @ApiOperation(value="收银台页面信息",notes="收银台页面信息",response = Response.class)
    @RequestMapping(value = "sel_cashierDesk", method = RequestMethod.GET)
    public Response selectCashierDeskInfo(@ApiParam(value = "订单编号") @RequestParam String orderNo)
    {
        Map<String,Object> cashierDesk = orderManagerService.selectCashierDeskInfo(orderNo,TechConstant.IsUseZhb.YES.toString(),TechConstant.CASHIER_PAYMENT_DURATION);
        Response response = new Response();
        response.setData(cashierDesk);
        return response;
    }

    @ApiOperation(value="收银台页面使用筑慧币",notes="收银台页面使用筑慧币",response = Response.class)
    @RequestMapping(value = "sel_cashierDeskUseZhb", method = RequestMethod.GET)
    public Response useZhbCashierDesk(@ApiParam(value = "订单编号") @RequestParam String orderNo,
                                          @ApiParam(value = "是否使用筑慧币 0：未使用，1:使用") @RequestParam String isUseZhb)
    {
        Map<String,Object> cashierDesk = orderManagerService.useZhbByCashierDesk(orderNo,Integer.parseInt(isUseZhb));
        Response response = new Response();
        response.setData(cashierDesk);
        return response;
    }

    @ApiOperation(value="技术培训课程下单获取验证码",notes="技术培训课程下单获取验证码",response = Response.class)
    @RequestMapping(value = "get_mobileCode", method = RequestMethod.GET)
    public Response get_TrainMobileCode(@ApiParam(value = "手机号") @RequestParam String mobile,
                                        @ApiParam(value ="图形验证码") @RequestParam String imgCode) throws IOException, ApiException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(true);
        String sessImgCode = (String) sess.getAttribute(TechConstant.MOBILE_CODE_SESSION_ORDER_CLASS);
        if(imgCode.equalsIgnoreCase(sessImgCode)) {
            expertService.getTrainMobileCode(mobile, TechConstant.MOBILE_CODE_SESSION_ORDER_CLASS);
        }else{
            throw new BusinessException(MsgCodeConstant.validate_error, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.validate_error)));
        }
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

    /**
     * 邮箱注册时的图形验证码
     * @param response
     */
    @ApiOperation(value="购买培训课程图形验证码",notes="购买培训课程图形验证码",response = Response.class)
    @RequestMapping(value = "sel_imgCode", method = RequestMethod.GET)
    public void getCode(HttpServletResponse response) throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session sess = currentUser.getSession(false);
        String verifyCode = VerifyCodeUtils.outputHttpVerifyImage(100,40,response, Constants.CHECK_IMG_CODE_SIZE);
        sess.setAttribute(TechConstant.MOBILE_CODE_SESSION_ORDER_CLASS, verifyCode);
    }

    /**
     * 记录发票信息
     */
    @ApiOperation(value="订单新增记录发票历史信息",notes="订单新增记录发票历史信息",response = Response.class)
    @RequestMapping(value = "add_invoiceRecord", method = RequestMethod.POST)
    public void insertInvoiceRecord(@ApiParam(value = "发票抬头类型：1个人，2企业") @RequestParam String invoiceTitleType,
                                    @ApiParam(value = "发票抬头公司名称") @RequestParam String invoiceTitle,
                                    @ApiParam(value = "发票类型：1增值税普通发票，2增值税专用发票") @RequestParam String invoiceType,
                                    @ApiParam(value = "发票收件人名称") @RequestParam String receiveName,
                                    @ApiParam(value = "省") @RequestParam String province,
                                    @ApiParam(value = "市") @RequestParam String city,
                                    @ApiParam(value = "区") @RequestParam String area,
                                    @ApiParam(value = "地址") @RequestParam String address,
                                    @ApiParam(value = "手机") @RequestParam String mobile,
                                    @ApiParam(value = "收件人固定电话") @RequestParam(required = false) String telephone
    ) {
        Long createid = ShiroUtil.getCreateID();
        if(createid != null) {
            InvoiceRecord record = new InvoiceRecord();
            record.setCreateId(createid);
            record.setIsRecentUsed(OrderConstants.InvoiceIsRecentUsed.NO.toString());
            //更新以前创建的记录为未使用
            invoiceService.updateIsRecentUsed(record);
            record.setInvoiceTitleType(Integer.parseInt(invoiceTitleType));
            record.setInvoiceTitle(invoiceTitle);
            record.setInvoiceType(invoiceType);
            record.setReceiveName(receiveName);
            record.setProvince(province);
            record.setCity(city);
            record.setArea(area);
            record.setAddress(address);
            record.setMobile(mobile);
            record.setTelephone(telephone);
            record.setIsRecentUsed(OrderConstants.InvoiceIsRecentUsed.YES.toString());
            invoiceService.insertInvoiceRecord(record);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    /**
     * 记录发票信息
     */
    @ApiOperation(value="订单更新记录发票历史信息",notes="订单更新记录发票历史信息",response = Response.class)
    @RequestMapping(value = "upd_invoiceRecord", method = RequestMethod.POST)
    public void updateInvoiceRecord(@ApiParam(value = "发票ID") @RequestParam String invoiceId,
                                    @ApiParam(value = "发票抬头类型：1个人，2企业") @RequestParam String invoiceTitleType,
                                    @ApiParam(value = "发票抬头公司名称") @RequestParam String invoiceTitle,
                                    @ApiParam(value = "发票类型：1增值税普通发票，2增值税专用发票") @RequestParam String invoiceType,
                                    @ApiParam(value = "发票收件人名称") @RequestParam String receiveName,
                                    @ApiParam(value = "省") @RequestParam String province,
                                    @ApiParam(value = "市") @RequestParam String city,
                                    @ApiParam(value = "区") @RequestParam String area,
                                    @ApiParam(value = "地址") @RequestParam String address,
                                    @ApiParam(value = "手机") @RequestParam String mobile,
                                    @ApiParam(value = "收件人固定电话") @RequestParam(required = false) String telephone
    ) {
        Long createid = ShiroUtil.getCreateID();
        if(createid != null) {
            InvoiceRecord record = new InvoiceRecord();
            record.setCreateId(createid);
            record.setIsRecentUsed(OrderConstants.InvoiceIsRecentUsed.NO.toString());
            //更新以前创建的记录为未使用
            invoiceService.updateIsRecentUsed(record);
            record.setInvoiceTitleType(Integer.parseInt(invoiceTitleType));
            record.setInvoiceTitle(invoiceTitle);
            record.setInvoiceType(invoiceType);
            record.setReceiveName(receiveName);
            record.setProvince(province);
            record.setCity(city);
            record.setArea(area);
            record.setAddress(address);
            record.setMobile(mobile);
            record.setTelephone(telephone);
            record.setIsRecentUsed(OrderConstants.InvoiceIsRecentUsed.YES.toString());
            invoiceService.updateInvoiceRecord(record);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
    }

    @ApiOperation(value="获取历史记录的发票信息",notes="获取历史记录的发票信息",response = Response.class)
    @RequestMapping(value = "sel_invoiceRecord", method = RequestMethod.GET)
    public Response getInvoiceRecord() throws IOException {
        Long createid = ShiroUtil.getCreateID();
        Response response = new Response();
        if(createid != null) {
            InvoiceRecord record = invoiceService.queryRecentUseInvoiceInfo(createid,OrderConstants.InvoiceIsRecentUsed.YES.toString());
            response.setData(record);
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}
