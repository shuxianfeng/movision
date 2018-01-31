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
 * 微信支付相关接口
 */
@RestController
@RequestMapping("/app/wepay/")
public class WepayController {

    @Autowired
    private WepayFacade wepayFacade;

    /**
     * 微信支付入参拼装（微信支付请求入参拼装签名和加密）
     */
    @ApiOperation(value = "微信支付入参拼装", notes = "微信支付请求入参拼装签名和加密", response = Response.class)
    @RequestMapping(value = "packageWePayParam", method = RequestMethod.POST)
    public Response packageWePayParam(@ApiParam(value = "订单id（多个用英文逗号隔开）") @RequestParam String ordersid) throws UnsupportedEncodingException, AlipayApiException {
        Response response = new Response();

        Map<String, Object> parammap = wepayFacade.packageWePayParam(ordersid);

        if (response.getCode() == 200 && (int) parammap.get("code") == 200) {
            response.setMessage("拼参及微信签名生成成功");
            response.setData(parammap);
        } else if ((int) parammap.get("code") == 300) {
            response.setCode(300);
            response.setMessage("请求的订单号中有订单被取消或订单不存在");
        } else if (response.getCode() != 200) {
            response.setMessage("拼参及微信签名生成失败");
        }
        return response;
    }
}
