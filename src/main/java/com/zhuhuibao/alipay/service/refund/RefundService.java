package com.zhuhuibao.alipay.service.refund;

import com.zhuhuibao.alipay.service.AlipayService;
import org.springframework.stereotype.Service;

/**
 * 即时到账批量退款接口 （refund_fastpay_by_platform_pwd）
 */
@Service
public class RefundService extends AlipayService {

    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = "refund_fastpay_by_platform_pwd";

    
}
