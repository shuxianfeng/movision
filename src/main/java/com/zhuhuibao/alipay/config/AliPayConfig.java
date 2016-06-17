package com.zhuhuibao.alipay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 支付宝公共配置
 */
@Component
public class AliPayConfig {

    @Value("${partner}")
    private String partner;
    @Value("${key}")
    private String key;
    @Value("${input_charset}")
    private String inputCharset;
    @Value("${sign_type}")
    private String signType;
    @Value("${alipay_gateway_new}")
    private String alipayGatewayNew;
    @Value("${enable_timestamp}")
    private boolean enableTimestamp;
    @Value("${payment_type}")
    private String paymentType;


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInputCharset() {
        return inputCharset;
    }

    public void setInputCharset(String inputCharset) {
        this.inputCharset = inputCharset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getAlipayGatewayNew() {
        return alipayGatewayNew;
    }

    public void setAlipayGatewayNew(String alipayGatewayNew) {
        this.alipayGatewayNew = alipayGatewayNew;
    }

    public boolean isEnableTimestamp() {
        return enableTimestamp;
    }

    public void setEnableTimestamp(boolean enableTimestamp) {
        this.enableTimestamp = enableTimestamp;
    }
}
