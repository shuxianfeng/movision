package com.zhuhuibao.business.pay;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 筑慧币付款，消费流程
 *
 * @author pl
 * @version 2016/6/21 0021
 */
@RestController
@RequestMapping(value ="/rest/zhbpayment/site/base")
@Api(value="ZhbPayment", description="筑慧币付款")
public class ZhbPaymentController {
    @Autowired
    PaymentService paymentService;

    @ApiOperation(value = "商品是否已经购买", notes = "商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_payment", method = RequestMethod.GET)
    public Response previewResume(@ApiParam(value = "商品ID") @RequestParam String GoodsID,
                                  @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
        Response response = paymentService.viewGoodsRecord(Long.parseLong(GoodsID),"", ZhbPaymentConstant.goodsType.CXXZJL.toString());
        return response;
    }
}
