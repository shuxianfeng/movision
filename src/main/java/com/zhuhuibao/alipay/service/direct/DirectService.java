package com.zhuhuibao.alipay.service.direct;

import com.zhuhuibao.alipay.config.AliPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 即时到账接口 （create_direct_pay_by_user）
 */
@Service
public class DirectService{


    @Autowired
    private AliPayConfig config;
    /**
     * 接口服务名称
     */
    public static final String SERVICE_NAME = "create_direct_pay_by_user";


    //        paramMap.put("service", SERVICE_NAME);
    //        paramMap.put("out_trade_no", out_trade_no);
//        paramMap.put("subject", subject);
//        paramMap.put("total_fee", total_fee);
//        paramMap.put("body", body);


}
