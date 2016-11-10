package com.zhuhuibao.mobile.web.pay;

import java.io.IOException;

import com.wordnik.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wordnik.swagger.annotations.ApiParam;


import com.zhuhuibao.common.Response;
import com.zhuhuibao.service.MobileExportResumeService;
import com.zhuhuibao.service.payment.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;

/**
 * 筑慧币付款，消费流程
 * @author zhuangyuhao
 * @time   2016年10月14日 下午3:02:25
 *
 */
@RestController
@RequestMapping(value ="/rest/m/zhbpayment/site/base")
@Api(value="MobileZhbPayment", description="筑慧币付款")
public class MobileZhbPaymentController {
    @Autowired
    PaymentService paymentService;
    
    @Autowired
    MobileExportResumeService mobileExportResumeSV;
    
    @ApiOperation(value = "商品是否已经购买", notes = "商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_payment", method = RequestMethod.GET)
    public Response viewGoodsPayInfo(@ApiParam(value = "商品ID") @RequestParam String GoodsID,
                                  @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
    	
        return paymentService.viewGoodsRecord(Long.parseLong(GoodsID),type);
    }
    
    @ApiOperation(value = "简历查看", notes = "商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_paymentResume", method = RequestMethod.GET)
    public Response previewResumeNew(@ApiParam(value = "商品ID") @RequestParam String GoodsID,
                                  @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
    	
        Response response = paymentService.viewResumeRecord(Long.parseLong(GoodsID),type);
        return response;
    }
    
    @RequestMapping(value="export_resume", method = RequestMethod.GET)
    @ApiOperation(value="定义简历模板导出简历",notes = "定义简历模板导出简历")
    public void exportResume(HttpServletRequest req, HttpServletResponse response,
                             @ApiParam(value = "简历ID") @RequestParam Long resumeID) throws IOException
    {
    	mobileExportResumeSV.exportResume(req, response, resumeID);
    }
}
