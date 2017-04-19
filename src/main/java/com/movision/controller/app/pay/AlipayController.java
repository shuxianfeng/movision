package com.movision.controller.app.pay;

import com.alipay.api.AlipayApiException;
import com.movision.common.Response;
import com.movision.facade.pay.AlipayFacade;
import com.movision.mybatis.record.entity.Record;
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
 * @Date 2017/3/14 10:10
 * 支付宝支付服务端接口
 */
@RestController
@RequestMapping("/app/alipay/")
public class AlipayController {

    @Autowired
    private AlipayFacade alipayFacade;

    /**
     * 入参拼装（支付宝支付请求入参拼装签名和加密）
     */
    @ApiOperation(value = "支付入参拼装", notes = "支付宝支付请求入参拼装签名和加密", response = Response.class)
    @RequestMapping(value = "packagePayParam", method = RequestMethod.POST)
    public Response packagePayParam(@ApiParam(value = "订单id（多个用英文逗号隔开）") @RequestParam String ordersid) throws UnsupportedEncodingException, AlipayApiException {
        Response response = new Response();

        Map<String, Object> parammap = alipayFacade.packagePayParam(ordersid);

        if (response.getCode() == 200 && (int) parammap.get("code") == 200) {
            response.setMessage("拼参及支付宝签名生成成功");
            response.setData(parammap);
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单号中有订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("拼参及支付宝签名生成失败");
        }
        return response;
    }

    /**
     * 交易退款接口
     *
     * @return
     */
    @ApiOperation(value = "交易退款接口", notes = "用于支付宝退款接口", response = Response.class)
    @RequestMapping(value = "tradingARefund", method = RequestMethod.POST)
    public Response tradingARefund(@ApiParam(value = "订单id（单个订单，一次只能退一个订单）") @RequestParam String orderid) throws AlipayApiException {
        Response response = new Response();
        Map<String, Object> refund = alipayFacade.tradingARefund(orderid);
        if (response.getCode() == 200 && (int) refund.get("code") == 200) {
            response.setCode(400);
            response.setMessage("退款交易成功");
            response.setData(refund.get("type"));
        } else if ((int) refund.get("code") == 300) {
            response.setCode(300);
            response.setMessage(refund.get("msg").toString());
        }

        return response;
    }

    /**
     * 支付宝支付订单的查询
     *
     * @return
     */
    @ApiOperation(value = "支付宝支付订单的查询", notes = "支付宝支付订单的查询", response = Response.class)
    @RequestMapping(value = "alipay_trade_query", method = RequestMethod.POST)
    public Response alipayTradeQuery(@ApiParam(value = "订单号（单个订单号）") @RequestParam String orderid) throws AlipayApiException {
        Response response = new Response();
        Map map = alipayFacade.alipayTradeQuery(orderid);
        if (response.getCode() == 200 && (int) map.get("code") == 200) {
            response.setMessage("支付宝支付订单的查询成功");
            response.setData(map);
        } else if ((int) map.get("code") == 300) {
            response.setCode(300);
            response.setMessage(map.get("msg").toString());
        }
        return response;
    }

    /**
     * 支付宝交易退款查询
     *
     * @param orderid
     * @return
     */
    @ApiOperation(value = "支付宝交易退款查询", notes = "用于支付宝交易退款查询接口", response = Response.class)
    @RequestMapping(value = "trading_refund_query", method = RequestMethod.POST)
    public Response tradingRefundQuery(@ApiParam(value = "订单号（单个订单号）") @RequestParam String orderid) throws AlipayApiException {
        Response response = new Response();
        Map map = alipayFacade.tradingRefundQuery(orderid);
        if (response.getCode() == 200 && (int) map.get("code") == 200) {
            response.setMessage(map.get("msg").toString());
        } else if ((int) map.get("code") == 300) {
            response.setCode(300);
            response.setMessage(map.get("msg").toString());
        }
        response.setData(map);
        return response;
    }
}
