package com.movision.controller.app.pay;

import com.alipay.api.AlipayApiException;
import com.movision.common.Response;
import com.movision.facade.pay.AlipayFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

/**
 * @Author shuxf
 * @Date 2017/3/14 15:09
 */
@RestController
@RequestMapping("/app/alipayback/")
public class AlipayBackController {

    @Autowired
    private AlipayFacade alipayFacade;

    /**
     * 支付宝支付回调接口
     */
    @ApiOperation(value = "支付宝支付完成后APP同步回调通知接口", notes = "用于支付宝支付完成后APP同步回调通知接口（支付宝APP支付目前不支持异步服务器回调，使用前台回调）", response = Response.class)
    @RequestMapping(value = "alipayback", method = RequestMethod.POST)
    public Response alipayback(@ApiParam(value = "结果码(类型为字符串)") @RequestParam String resultStatus,//9000 订单支付成功 8000 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
                               //4000 订单支付失败 5000 重复请求 6001 用户中途取消 6002 网络连接出错 6004 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态     其它   其它支付错误
                               @ApiParam(value = "处理结果(类型为json结构字符串)") @RequestParam String result) throws AlipayApiException, ParseException {
        Response response = new Response();

        int flag = alipayFacade.alipayback(resultStatus, result);

        if (response.getCode() == 200 && flag == 1) {
            response.setMessage("回调通知成功");
        } else {
            response.setCode(300);
            response.setMessage("验签失败或回调通知失败");
        }
        return response;
    }
}
