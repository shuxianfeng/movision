package com.movision.controller.app.pay;

import com.alipay.api.AlipayApiException;
import com.movision.common.Response;
import com.movision.facade.pay.WepayFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/1/31 9:34
 * 小程序支付相关接口
 */
@RestController
@RequestMapping("/app/wepay/")
public class WepayController {

    @Autowired
    private WepayFacade wepayFacade;

    /**
     * 微信支付：统一下单接口
     */
    @ApiOperation(value = "微信支付统一下单接口", notes = "微信支付统一下单接口", response = Response.class)
    @RequestMapping(value = "getWePay", method = RequestMethod.POST)
    public Response getWePay(@ApiParam(value = "微信openid") @RequestParam String openid,
                             @ApiParam(value = "订单id（订单表的主键id，非订单编号）") @RequestParam String ordersid) throws UnsupportedEncodingException, AlipayApiException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.getWePay(openid, ordersid);

        if (response.getCode() == 200 && (int) parammap.get("code") == 200) {
            response.setMessage("统一下单接口请求成功");
            response.setData(parammap);
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("拼参及微信签名生成失败");
        }
        return response;
    }

    /**
     * 微信支付：查询订单接口
     */
    @ApiOperation(value = "微信支付查询订单接口", notes = "微信支付查询订单接口", response = Response.class)
    @RequestMapping(value = "queryOrderInfo", method = RequestMethod.POST)
    public Response queryOrderInfo(@ApiParam(value = "微信订单号（微信的订单号，优先使用 ）") @RequestParam(required = false) String transactionid,
                                   @ApiParam(value = "订单id（订单表的主键id，非订单编号）") @RequestParam(required = false) String ordersid) throws UnsupportedEncodingException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.queryOrderInfo(transactionid, ordersid);

        if (response.getCode() == 200 && (int) parammap.get("code") == 200) {
            response.setMessage("订单查询成功");
            response.setData(parammap);
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("订单查询失败");
        }
        return response;
    }
}