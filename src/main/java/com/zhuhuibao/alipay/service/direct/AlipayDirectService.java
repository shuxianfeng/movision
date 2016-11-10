package com.zhuhuibao.alipay.service.direct;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.zhuhuibao.alipay.config.AliPayConfig;
import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.mybatis.order.entity.OrderFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 即时到账接口 （create_direct_pay_by_user）
 */
@Service
public class AlipayDirectService {
    private static final Logger log = LoggerFactory.getLogger(AlipayDirectService.class);

    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = AlipayPropertiesLoader.getPropertyValue("direct_service");
    /**
     * 支付宝异步通知
     */
    public static final String NOTIFY_URL = AlipayPropertiesLoader.getPropertyValue("direct_notify_url");
    /**
     * 支付宝同步通知
     */
    public static final String RETURN_URL = AlipayPropertiesLoader.getPropertyValue("direct_return_url");

    @Autowired
    private AlipayService alipayService;

    @Autowired
    private AliPayConfig aliPayConfig;

    /**
     * 支付宝支付
     *
     * @param paramMap
     *            请求参数集合
     * @return
     */
    public String alipayRequst(Map<String, String> paramMap) throws Exception {

        paramMap.put("service", SERVICE_NAME);
        String noticeTag = AlipayPropertiesLoader.getPropertyValue("alipay_back_switch");
        if (noticeTag.equals("sync")) {
            paramMap.put("returnUrl", RETURN_URL); // 同步通知
        } else if (noticeTag.equals("async")) {
            paramMap.put("notifyUrl", NOTIFY_URL); // 异步通知
        }

        return alipayService.alipay(paramMap, PayConstants.ALIPAY_METHOD_GET);

    }

    /**
     * 下单支付业务逻辑
     * 
     * @param resp
     *            HttpServletResponse
     * @param paramMap
     *            params
     * @throws Exception
     */
    public void doPay(HttpServletResponse resp, Map<String, String> paramMap) throws Exception {
        resp.setContentType("text/html;charset=utf-8");

        log.info("***********************进入支付环节********************");

        String sHtmlText = alipayRequst(paramMap);
        log.info("支付宝支付请求页面:{}", sHtmlText);

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            out.write(sHtmlText);

        } catch (IOException e) {
            e.printStackTrace();
            log.error("获取输出流异常:" + e.getMessage());
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * 支付宝h5支付请求
     * 
     * @param httpResponse
     * @throws ServletException
     * @throws IOException
     * @throws AlipayApiException
     */
    public void h5Pay(HttpServletResponse httpResponse, OrderFlow orderFlow, Map<String, String> msgParam) throws ServletException, IOException, AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayPropertiesLoader.getPropertyValue("h5_alipay_gateway"), AlipayPropertiesLoader.getPropertyValue("app_id"),
                AlipayPropertiesLoader.getPropertyValue("h5_alipay_private_key"), "json", aliPayConfig.getInputCharset(), AlipayPropertiesLoader.getPropertyValue("h5_alipay_public_key"));
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();// 创建API对应的request
        alipayRequest.setReturnUrl(AlipayPropertiesLoader.getPropertyValue("h5_direct_return_url"));
        alipayRequest.setNotifyUrl(AlipayPropertiesLoader.getPropertyValue("h5_direct_notify_url"));// 在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{"+"\"out_trade_no\":"+orderFlow.getOrderNo()+","+""+"\"total_amount\":"+orderFlow.getTradeFee()+","+"\"subject\":"+msgParam.get("goodsName")+","+"\"seller_id\":,"+AlipayPropertiesLoader.getPropertyValue("seller_id")+"\"product_code\":"+orderFlow.getOrderNo()+"}");// 填充业务参数
        String form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
        httpResponse.setContentType("text/html;charset=" + aliPayConfig.getInputCharset());
        httpResponse.getWriter().write(form);// 直接将完整的表单html输出到页面
        httpResponse.getWriter().flush();
    }
}
