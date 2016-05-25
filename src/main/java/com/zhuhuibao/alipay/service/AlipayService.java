package com.zhuhuibao.alipay.service;

import com.zhuhuibao.alipay.config.AliPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝服务入口
 */
@Service
public class AlipayService {

    @Autowired
    private AliPayConfig config;

    /**
     * 组装基本参数
     * @return paramMap
     */
    private Map<String,String> assBasicParmas(){
        Map<String,String> paramMap = new HashMap<>();

        paramMap.put("partner", config.getPartner());
        paramMap.put("seller_id", config.getSellerId());
        paramMap.put("_input_charset", config.getInputCharset());
        paramMap.put("payment_type", config.getPaymentType());
        paramMap.put("notify_url", config.getNotifyUrl());
        paramMap.put("return_url", config.getReturnUrl());
        paramMap.put("anti_phishing_key", config.getAntiPhishingKey());
        paramMap.put("exter_invoke_ip", config.getExterInvokeIp());

        return paramMap;
    }

    /**
     * 建立请求
     * @param paramMap 请求参数集合
     * @param method 提交方式。两个值可选：post、get
     * @return
     */
    private String buildRequest(Map<String,String> paramMap,String method){

        return "";
    }

}
