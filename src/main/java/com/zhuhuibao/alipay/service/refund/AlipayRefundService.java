package com.zhuhuibao.alipay.service.refund;

import com.zhuhuibao.alipay.service.AlipayService;
import com.zhuhuibao.alipay.util.AlipayPropertiesLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 即时到账批量退款接口 （refund_fastpay_by_platform_pwd）
 */
@Service
public class AlipayRefundService {

    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = AlipayPropertiesLoader.getPropertyValue("refund_service");
    public static final String NOTIFY_URL =  AlipayPropertiesLoader.getPropertyValue("refund_notify_url");
//    public static final String RETURN_URL =  AlipayPropertiesLoader.getPropertyValue("refund_return_url");

    @Autowired
    private AlipayService alipayService;


    
}
