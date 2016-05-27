package com.zhuhuibao.business.pay;

import com.zhuhuibao.alipay.util.AlipayNotify;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.utils.PropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付  {回调接口} callback
 */
@RestController
@RequestMapping("/rest/alipay")
public class AlipayController {
    private static final Logger log = LoggerFactory.getLogger(AlipayController.class);


    /**
     * 支付宝同步跳转 {return_url}      GET
     *
     * @param request http 请求
     */
    @RequestMapping(value = "callback/return", method = RequestMethod.GET)
    public ModelAndView alipaySynchronization(HttpServletRequest request) {
        log.debug("*****支付宝同步跳转*****开始");
        ModelAndView modelAndView = new ModelAndView();
        RedirectView rv = new RedirectView(PropertiesUtils.getValue("host.ip")+"/"+PropertiesUtils.getValue("alipay_return_url"));
        modelAndView.setView(rv);
        try{
            // 获取返回信息
            // 获取支付宝GET过来反馈信息
            Map<String, String> params = getRequestParams(request);

            // 计算得出通知验证结果
            log.info("******支付宝同步回调校验参数信息开始*******");
            boolean verify_result = AlipayNotify.verify(params);
            log.info("******支付宝同步回调校验参数信息结果=" + verify_result);

            if(verify_result){ //验证成功

                if (params.get("trade_status").equals("TRADE_FINISHED")
                        || params.get("trade_status").equals("TRADE_SUCCESS")) {
                    Map<String, String> resultMap = tradeSuccessDeal(params,
                            PayConstants.NotifyType.CDX.toString());
                    log.info("***同步回调：支付平台回调发起发支付方结果：" + resultMap);
                    if (resultMap != null
                            && String
                            .valueOf(PayConstants.HTTP_SUCCESS_CODE)
                            .equals(resultMap.get("statusCode"))) {
                        if ("SUCCESS".equals(resultMap.get("result"))) {
                            modelAndView.addObject("result","success");
                            modelAndView.addObject("msg","支付成功");
                        }
                    }
                } else {
                    modelAndView.addObject("result","fail");
                    modelAndView.addObject("msg","支付成功");
                }
            }else{ //验证失败
                modelAndView.addObject("result","fail");
                modelAndView.addObject("msg","验证失败");
            }


        }catch(Exception e){
            log.error("获取out对象异常" + e.getMessage());
            e.printStackTrace();
        }


        return modelAndView;
    }

    /**
     * 支付宝异步跳转 {notify_url}      POST
     *
     * @param request  http 请求
     * @param response http
     */
    @RequestMapping(value = "callback/notify", method = RequestMethod.POST)
    public void alipayAsynchronous(HttpServletRequest request, HttpServletResponse response) {
        log.debug("*******支付宝异步跳转******开始");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            // 获取支付宝POST过来反馈信息
            Map<String, String> params = getRequestParams(request);

            log.info("******支付宝异步回调校验参数信息开始*******");
            boolean verify_result = AlipayNotify.verify(params);
            log.info("******支付宝异步回调校验参数信息结果=" + verify_result);

            if (verify_result) {// 验证成功

                if (params.get("trade_status").equals("TRADE_FINISHED")
                        || params.get("trade_status").equals("TRADE_SUCCESS")) {
                    Map<String, String> resultMap =
                            tradeSuccessDeal(params, PayConstants.NotifyType.DDD.toString());
                    log.info("***异步回调：支付平台回调发起发支付方结果：" + resultMap);
                    if (resultMap != null
                            && String.valueOf(PayConstants.HTTP_SUCCESS_CODE).equals(resultMap.get("statusCode"))) {
                        if ("SUCCESS".equals(resultMap.get("result"))) {
                            out.println("success");
                        }
                    }
                }

            } else {// 验证失败
                out.println("fail");
            }
        } catch (IOException e) {
            log.error("获取out对象异常" + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取请求参数
     *
     * @param request http 请求
     * @return map
     * @throws UnsupportedEncodingException
     */
    public Map<String, String> getRequestParams(HttpServletRequest request) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Object o : requestParams.keySet()) {
            String name = (String) o;
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
                log.info("valueStr=" + valueStr);
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        log.info("*********返回参数信息params:" + params + "***********");
        return params;
    }

    /**
     * 返回成功时业务逻辑处理
     *
     * @param params 请求参数
     * @param notifyType 通知类型
     * @return
     */
    public Map<String, String> tradeSuccessDeal(Map<String, String> params, String notifyType) {
        log.debug("支付返回成功,业务逻辑处理...");
        Map<String,String> resultMap = new HashMap<>();

        //生成一条交易流水记录


        resultMap.put("statusCode", String.valueOf(PayConstants.HTTP_SUCCESS_CODE));
        return resultMap;
    }
}
