package com.zhuhuibao.business.pay;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.common.constant.PayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付宝支付  {回调接口} callback
 */
@RestController
@RequestMapping("/rest/alipay")
public class AlipayController {
    private static final Logger log = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    AlipayService alipayService;

    /**
     * 即时到账接口
     * <p/>
     * 支付宝同步跳转 {return_url}      GET
     *
     * @param request http 请求
     */
    @RequestMapping(value = "callback/return", method = RequestMethod.GET)
    public ModelAndView alipaySynchPay(HttpServletRequest request) {
        log.debug("*****支付宝同步跳转*****开始");

        return alipayService.syncNotify(request, PayConstants.TradeType.PAY.toString());
    }


    /**
     * 即时到账接口
     * <p/>
     * 支付宝异步跳转 {notify_url}      POST
     *
     * @param request  http 请求
     * @param response http
     */
    @RequestMapping(value = "callback/direct_notify", method = RequestMethod.POST)
    public void alipayAsynPay(HttpServletRequest request, HttpServletResponse response) {
        log.debug("*******支付宝[即时到账]异步跳转******开始");

        alipayService.asyncNotify(request, response, PayConstants.TradeType.PAY.toString());
    }

    /**
     * 即时到账接口
     * <p/>
     * 支付宝异步跳转 {notify_url}      POST
     *
     * @param request  http 请求
     * @param response http
     */
    @RequestMapping(value = "callback/refund_notify", method = RequestMethod.POST)
    public void alipayAsynRefund(HttpServletRequest request, HttpServletResponse response) {
        log.debug("*******支付宝[批量退款]异步跳转******开始");

        alipayService.asyncNotify(request, response, PayConstants.TradeType.REFUND.toString());
    }

}
