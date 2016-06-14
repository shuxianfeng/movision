package com.zhuhuibao.alipay.service.direct;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.OrderConstants;
import com.zhuhuibao.common.constant.PayConstants;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.PublishCourse;
import com.zhuhuibao.mybatis.order.service.PublishCourseService;
import com.zhuhuibao.zookeeper.DistributedLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public static final String NOTIFY_URL =  AlipayPropertiesLoader.getPropertyValue("direct_notify_url");
    /**
     * 支付宝同步通知
     */
//    public static final String RETURN_URL =  AlipayPropertiesLoader.getPropertyValue("direct_return_url");

    @Autowired
    private AlipayService alipayService;



    /**
     * 下单支付业务逻辑
     * @param resp   HttpServletResponse
     * @param paramMap  params
     * @throws Exception
     */
    public void doPay(HttpServletResponse resp, Map<String, String> paramMap) throws Exception {
        resp.setContentType("text/html;charset=utf-8");

        log.info("***********************进入支付环节********************");

        String  sHtmlText = alipayRequst(paramMap);
        log.info("支付宝支付请求页面:{}", sHtmlText);

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
     * 支付宝支付
     *
     * @param paramMap 请求参数集合
     * @return
     */
    public String alipayRequst(Map<String, String> paramMap) throws Exception {

        paramMap.put("service" ,SERVICE_NAME);
//        paramMap.put("returnUrl", RETURN_URL); //同步通知
        paramMap.put("notifyUrl", NOTIFY_URL);   //异步通知

        return  alipayService.alipay(paramMap, PayConstants.ALIPAY_METHOD_GET);

    }

}
