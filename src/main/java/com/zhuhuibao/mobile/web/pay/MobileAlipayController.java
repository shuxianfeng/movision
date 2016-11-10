package com.zhuhuibao.mobile.web.pay;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
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
 * 手机端 支付宝支付 {回调接口} callback
 */
@RestController
@RequestMapping("/rest/m/alipay")
public class MobileAlipayController {
    private static final Logger log = LoggerFactory.getLogger(MobileAlipayController.class);

    @Autowired
    AlipayService alipayService;

    /**
     * 支付宝同步[即时到账]
     * <p/>
     * 支付宝同步跳转 {return_url} GET
     *
     * @param request
     *            http 请求
     */
    @RequestMapping(value = "callback/direct_return", method = RequestMethod.GET)
    public ModelAndView alipaySynchPay(HttpServletRequest request) {
        log.debug("*****H5支付宝同步[即时到账]跳转*****开始");
        String returnUrl = AlipayPropertiesLoader.getPropertyValue("h5_alipay_return_url");
        return alipayService.syncNotify(request, PayConstants.TradeType.PAY.toString(), returnUrl, "h5");
    }

    /**
     * 支付宝同步[批量退款]
     * <p/>
     * 支付宝同步跳转 {return_url} GET
     *
     * @param request
     *            http 请求
     */
    @RequestMapping(value = "callback/refund_return", method = RequestMethod.GET)
    public ModelAndView alipaySynchRefund(HttpServletRequest request) {

        log.debug("*****H5支付宝同步[批量退款]跳转*****开始");
        String returnUrl = AlipayPropertiesLoader.getPropertyValue("alirefund_return_url");
        return alipayService.syncNotify(request, PayConstants.TradeType.REFUND.toString(), returnUrl, "h5");
    }

    /**
     * 即时到账接口
     * <p/>
     * 支付宝异步跳转 {notify_url} POST
     *
     * @param request
     *            http 请求
     * @param response
     *            http
     */
    @RequestMapping(value = "callback/direct_notify", method = RequestMethod.POST)
    public void alipayAsynPay(HttpServletRequest request, HttpServletResponse response) {
        log.debug("*******支付宝[即时到账]异步跳转******开始");
        alipayService.asyncNotify(request, response, PayConstants.TradeType.PAY.toString(), "h5");
    }

    /**
     * 批量退款
     * <p/>
     * 支付宝异步跳转 {notify_url} POST
     *
     * @param request
     *            http 请求
     * @param response
     *            http
     */
    @RequestMapping(value = "callback/refund_notify", method = RequestMethod.POST)
    public void alipayAsynRefund(HttpServletRequest request, HttpServletResponse response) {

        log.debug("*******支付宝[批量退款]异步跳转******开始");

        alipayService.asyncNotify(request, response, PayConstants.TradeType.REFUND.toString(), "h5");
    }

}
