package com.movision.controller.app.pay;

import com.alipay.api.AlipayApiException;
import com.movision.common.Response;
import com.movision.facade.pay.WepayFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Date;
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

    /**
     * 微信支付：申请退款接口
     */
    @ApiOperation(value = "微信支付申请退款接口", notes = "微信支付申请退款接口", response = Response.class)
    @RequestMapping(value = "getRefund", method = RequestMethod.POST)
    public Response getRefund(@ApiParam(value = "订单id（订单表的主键id，非订单编号，前两个参数二选一）") @RequestParam(required = false) String ordersid,
                              @ApiParam(value = "微信订单号（微信的订单号，优先使用，前两个参数二选一）") @RequestParam(required = false) String transactionid,
                              @ApiParam(value = "退款金额（<=订单总额，默认退订单全额）") @RequestParam(required = false) String amount) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.getRefund(ordersid, transactionid, amount);

        if (response.getCode() == 200) {
            if ((int) parammap.get("code") == 200) {
                response.setMessage("退款成功");
                response.setData(parammap);
            }else if ((int) parammap.get("code") == 400){
                response.setCode(400);
                response.setMessage("输入的退款金额超过订单实付总金额");
            }else if ((int) parammap.get("code") == 300){
                response.setCode(300);
                response.setMessage("该订单状态并非已支付状态");
            }else if ((int) parammap.get("code") == 500){
                response.setCode(500);
                response.setMessage("HTTP POST ERROR!");
            }
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("退款失败");
        }
        return response;
    }

    /**
     * 微信支付：退款查询接口
     */
    @ApiOperation(value = "微信支付退款查询接口", notes = "使用优先级：refund_id > out_refund_no > transaction_id > out_trade_no", response = Response.class)
    @RequestMapping(value = "refundQuery", method = RequestMethod.POST)
    public Response refundQuery(@ApiParam(value = "微信订单号（微信订单号，前四个参数四选一）") @RequestParam(required = false) String transaction_id,
                                @ApiParam(value = "商户订单号（商户订单号即订单表主键id，前四个参数四选一）") @RequestParam(required = false) String out_trade_no,
                                @ApiParam(value = "商户退款单号（订单表订单编号ordernumber，前四个参数四选一）") @RequestParam(required = false) String out_refund_no,
                                @ApiParam(value = "微信退款单号（调用退款接口时微信返回的退款单号，前四个参数四选一）") @RequestParam(required = false) String refund_id) throws IOException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.refundQuery(transaction_id, out_trade_no, out_refund_no, refund_id);

        if (response.getCode() == 200) {
            if ((int) parammap.get("code") == 200) {
                response.setMessage("退款成功");
                response.setData(parammap);
            }else if ((int) parammap.get("code") == 400){
                response.setCode(400);
                response.setMessage("输入的退款金额超过订单实付总金额");
            }else if ((int) parammap.get("code") == 300){
                response.setCode(300);
                response.setMessage("该订单状态并非已支付状态");
            }else if ((int) parammap.get("code") == 500){
                response.setCode(500);
                response.setMessage("HTTP POST ERROR!");
            }
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("退款失败");
        }
        return response;
    }

    /**
     * 微信支付：下载对账单接口
     */
    @ApiOperation(value = "微信支付下载对账单接口", notes = "使用优先级：refund_id > out_refund_no > transaction_id > out_trade_no", response = Response.class)
    @RequestMapping(value = "downloadBill", method = RequestMethod.POST)
    public Response downloadBill(@ApiParam(value = "下载对账单的日期，格式：20180320") @RequestParam String billdate,
                                 @ApiParam(value = "账单类型:ALL，返回当日所有订单信息，默认值;SUCCESS，返回当日成功支付的订单;REFUND，返回当日退款订单;RECHARGE_REFUND，返回当日充值退款订单（相比其他对账单多一栏“返还手续费”）") @RequestParam String billtype) throws UnsupportedEncodingException, DocumentException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.downloadBill(billdate, billtype);

        if (response.getCode() == 200) {
            if ((int) parammap.get("code") == 200) {
                response.setMessage("账单下载成功");
                response.setData(parammap);
            }else if ((int) parammap.get("code") == 20002){
                response.setCode(20002);
                response.setMessage("对账单不存在");
            }else if ((int) parammap.get("code") == 20001){
                response.setCode(20001);
                response.setMessage("无效的对账单日期");
            }else {
                response.setCode(400);
                response.setMessage("账单下载失败");
            }
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("账单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("账单下载失败");
        }
        return response;
    }
}