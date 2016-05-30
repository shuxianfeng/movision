package com.zhuhuibao.alipay.service.refund;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.PayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Map;

/**
 * 即时到账批量退款接口 （refund_fastpay_by_platform_pwd）
 */
@Service
public class AlipayRefundService {
    private static final Logger log = LoggerFactory.getLogger(AlipayRefundService.class);
    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = AlipayPropertiesLoader.getPropertyValue("refund_service");

    public static final String NOTIFY_URL =  AlipayPropertiesLoader.getPropertyValue("refund_notify_url");
//    public static final String RETURN_URL =  AlipayPropertiesLoader.getPropertyValue("refund_return_url");

    @Autowired
    private AlipayService alipayService;

    /**
     * 批量退款
     * @param resp
     * @param paramMap
     */
    public void doRefund(HttpServletResponse resp, Map paramMap) throws Exception {
        log.debug("立即支付请求参数:{}", paramMap.toString());
        resp.setContentType("text/html;charset=utf-8");

        log.info("***********************进入批量退款环节********************");

        String  sHtmlText = refundRequst(paramMap);
        log.info("支付宝退款请求页面:{}", sHtmlText);

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.write(sHtmlText);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取输出流异常:" + e.getMessage());
        }finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }

    }

    /**
     * 调用支付宝批量退款接口
     * @param paramMap
     * @return
     */
    private String refundRequst(Map paramMap) throws Exception {
        paramMap.put("service" ,SERVICE_NAME);
//        paramMap.put("returnUrl", RETURN_URL); //同步通知
        paramMap.put("notifyUrl", NOTIFY_URL);   //异步通知

        return  alipayService.alirefund(paramMap, PayConstants.ALIPAY_METHOD_GET);
    }
}
